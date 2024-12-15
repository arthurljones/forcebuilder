package tech.ajones.forcebuilder.model

import kotlin.math.abs

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

enum class AvailableTechLevel {
  Any, Advanced, Standard
}

data class AvailabilityCriteria(
  val minEra: Era = Era.StarLeague,
  val maxEra: Era = Era.IlClan,
  val level: AvailableTechLevel = AvailableTechLevel.Any
)

class AvailableInEra(
  private val criteria: AvailabilityCriteria
): ForceRequirement {
  override fun unitCouldMeet(unit: UnitVariant): Boolean {
    val applicableDate = when (criteria.level) {
      AvailableTechLevel.Any -> unit.yearIntroduced
      AvailableTechLevel.Advanced -> unit.advancedTechYear
      AvailableTechLevel.Standard -> unit.standardTechYear
    }
    return applicableDate >= criteria.minEra.start && applicableDate <= criteria.maxEra.end
  }
}
