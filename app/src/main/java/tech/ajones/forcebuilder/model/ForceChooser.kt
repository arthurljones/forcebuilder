package tech.ajones.forcebuilder.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import java.util.PriorityQueue
import kotlin.coroutines.coroutineContext
import kotlin.math.abs

interface ForcePriority {
  fun scoreForce(force: Set<ChosenVariant>): Double
}

class MaximizePointsValue: ForcePriority {
  override fun scoreForce(force: Set<ChosenVariant>): Double = force.pvSum.toDouble()
}

/**
 * Score for whether a force meets a requirement. [distanceFromMatch] should be in range [0, 1],
 * where 0.0 meets the requirement and 1.0 is as far as possible away from meeting the
 * requirement. When comparing two non-zero scores, the one closer to zero is the one
 * closer to meeting the requirement.
 */
@JvmInline
value class RequirementScore(val distanceFromMatch: Double) {
  val meets: Boolean get() = distanceFromMatch == MEETS_SCORE
  companion object {
    const val MEETS_SCORE: Double = 0.0
    val requirementMet = RequirementScore(MEETS_SCORE)
  }
}

interface ForceRequirement {
  /**
   * Whether an individual unit could ever meet the requirement alone or as part of a force
   */
  fun unitCouldMeet(unit: UnitVariant): Boolean = true

  /**
   * How far [force] is from meeting this requirement
   */
  fun checkForce(force: Set<ChosenVariant>): RequirementScore = RequirementScore.requirementMet
}

private val Iterable<ChosenVariant>.pvSum: Int
  get() = sumOf { it.unit.pointsValue }

abstract class ValueRange(
  private val min: Double?,
  private val max: Double?,
  private val maxDistance: Double,
): ForceRequirement {
  abstract fun forceValue(force: Set<ChosenVariant>): Double

  init {
    if (min != null && max != null && min > max) {
      throw IllegalArgumentException("invalid range, min is more than max")
    }
  }

  override fun checkForce(force: Set<ChosenVariant>): RequirementScore {
    val value = forceValue(force)
    val limit = max?.takeIf { value > it }
      ?: min?.takeIf { value < it }
    val score = limit
      ?.let { abs(value - it).coerceAtMost(maxDistance) / maxDistance }
      ?: 0.0
    return RequirementScore(score)
  }
}

class PointValueRange(
  min: Int? = null,
  max: Int? = null,
): ValueRange(
  min = min?.toDouble(),
  max = max?.toDouble(),
  maxDistance = 1000.0
) {
  override fun forceValue(force: Set<ChosenVariant>) = force.pvSum.toDouble()
}

class UnitCountRange(
  min: Int? = null,
  max: Int? = null,
): ValueRange(
  min = min?.toDouble(),
  max = max?.toDouble(),
  maxDistance = 100.0
) {
  override fun forceValue(force: Set<ChosenVariant>) = force.size.toDouble()
}

class MatchingTechBase(private val techBases: Set<TechBase>): ForceRequirement {
  override fun unitCouldMeet(unit: UnitVariant): Boolean =
    if (unit.isClan) {
      techBases.contains(TechBase.Clan)
    } else {
      techBases.contains(TechBase.IS)
    }
}

class IncludesUnits(private val units: Set<ChosenVariant>): ForceRequirement {
  override fun checkForce(force: Set<ChosenVariant>): RequirementScore {
    val excludedCount = (units - force).size
    return RequirementScore(excludedCount.toDouble() / units.size)
  }
}

data class ScoredForce(
  val units: Set<ChosenVariant>,
  val score: ForceScore
) : Comparable<ScoredForce> {
  override fun compareTo(other: ScoredForce): Int = score.compareTo(other.score)
}

/**
 * "Better" scores are sorted as smaller
 */
data class ForceScore(
  val requirements: RequirementScore,
  val priorities: Double
) : Comparable<ForceScore> {
  override fun compareTo(other: ForceScore): Int {
    // two scores: requirements score and priority score
    //   any non-meeting requirements score should always compare less than a meeting
    //   requirement score, regardless of priority score.
    // if both scores meet requirements, then the largest priorities score "wins"
    val foo = if (!requirements.meets) {
      if (!other.requirements.meets) {
        requirements.distanceFromMatch.compareTo(other.requirements.distanceFromMatch)
      } else {
        1
      }
    } else {
      if (!other.requirements.meets) {
        -1
      } else {
        -priorities.compareTo(other.priorities)
      }
    }
    return foo
  }
}

class ForceScorer(
  private val requirements: List<ForceRequirement>,
  private val priority: ForcePriority
) {
  /**
   * Whether an individual unit could ever meet the requirement alone or as part of a force
   */
  fun unitCouldMeet(unit: UnitVariant): Boolean =
    requirements.all { it.unitCouldMeet(unit) }

  fun scoreForce(force: Set<ChosenVariant>): ScoredForce =
    requirements.sumOf { it.checkForce(force).distanceFromMatch }
      .let {
        val score = ForceScore(
          requirements = RequirementScore(it / requirements.size),
          priorities = priority.scoreForce(force),
        )
        ScoredForce(units = force, score = score)
      }
}

object ForceChooser {
  suspend fun chooseUnits(
    scorer: ForceScorer,
    allMinis: List<Mini>,
    initial: Set<ChosenVariant> = emptySet(),
    progress: MutableStateFlow<Float>? = null
  ): Set<ChosenVariant> {
    // TODO/Ideas:
    //  - Locked/forced units can be a force requirement, which adds its list in resolution
    //  - Requirements and priorities can rank next moves?

    val start = initial.toSet()
    var bestForce = scorer.scoreForce(start)

    // All visited forces
    val visited: MutableSet<Set<ChosenVariant>> = mutableSetOf(start)
    // All forces that have not been explored
    val open = PriorityQueue<ScoredForce>()
    open.offer(bestForce)
    val totalIterations = 100
    // How many iteration loops remain, so we don't spin forever
    var iterationsRemaining = totalIterations

    // Minis with variants that can never meet the requirements for this force pruned.
    // Minis with no remaining variants are also pruned.
    val allMinisPruned = allMinis
      // Shuffle for more random selection
      .shuffled()
      .map { origMini ->
        Mini(
          chassis = origMini.chassis,
          possibleUnits = origMini.possibleUnits
            .filter { scorer.unitCouldMeet(it) }
            .shuffled(),
          id = origMini.id
        )
      }
      .filter { it.possibleUnits.isNotEmpty() }
      .toSet()

    //println("chooseUnits: allMinis: ${allMinis.size} prunedMinis: ${allMinisPruned.size}")

    while (
      coroutineContext.isActive &&
      iterationsRemaining > 0
    // TODO: Additional/replacement end condition for either score above threshold or
    //  score change less than some amount over last N iterations
    ) {
      //println("iterating. open: ${open.size} visited: ${visited.size} remaining: $iterationsRemaining")
      val force = open.poll() ?: break
      //println("iterating. force: $force")
      val forceMinis = force.units.map { it.mini }.toSet()
      val availableMinis = allMinisPruned - forceMinis

      //println("availableMinis: ${availableMinis.size}")

      // For each unit in the force, generate a new potential force with that unit removed
      val nextRemovedUnits = force.units.toList()
        .let { units ->
          (0..units.lastIndex).map { idxToRemove ->
            units.filterIndexed { idx, _ -> idx != idxToRemove }.toSet()
          }
        }

      // For each available unit, generate a new potential force with that unit added
      val nextAddedUnits = availableMinis
        .flatMap { mini ->
          mini.possibleUnits.map {
            force.units + ChosenVariant(mini = mini, unit = it)
          }.toSet()
        }

      //println("next added permutations: ${nextAddedUnits.size}")

      sequence {
        yieldAll(nextRemovedUnits)
        yieldAll(nextAddedUnits)
      }.mapNotNull { potential ->
        potential
          .takeIf { !visited.contains(it) }
          ?.let { scorer.scoreForce(it) }
      }.sorted()
        .take(20)
        .forEach {
          if (it.score < bestForce.score) {
            bestForce = it
          }
          //println("adding $it to list")
          visited.add(it.units)
          open.offer(it)
        }
      //println("iterating: best: $bestForce")
      iterationsRemaining--
      progress?.value = (totalIterations - iterationsRemaining).toFloat() / totalIterations
    }

    //println("done. best: $bestForce")
    return bestForce.units
  }
}
