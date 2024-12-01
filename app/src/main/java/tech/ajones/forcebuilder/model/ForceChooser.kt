package tech.ajones.forcebuilder.model
interface ForceComparator: Comparator<List<ChosenVariant>>

sealed interface ForceRequirementMatch: Comparable<ForceRequirementMatch> {
  /**
   * The force in question fully matches the requirement
   */
  data object Full: ForceRequirementMatch
  /**
   * The force in question only partially matches the requirement, but is not forbidden
   */
  data class Partial(val matchPercentage: Double): ForceRequirementMatch
  /**
   * The force in question is forbidden by the requirement
   */
  data object Forbidden: ForceRequirementMatch

  override fun compareTo(other: ForceRequirementMatch): Int =
    when (this) {
      Forbidden -> {
        when(other) {
          Forbidden -> 0
          else -> -1
        }
      }
      is Partial -> {
        when (other) {
          Forbidden -> 1
          is Partial -> matchPercentage.compareTo(other.matchPercentage)
          Full -> -1
        }
      }
      Full -> {
        when (other) {
          Full -> 0
          else -> 1
        }
      }
    }
}


interface ForceRequirement {
  fun checkForce(force: List<ChosenVariant>): ForceRequirementMatch
}

class NoRequirement: ForceRequirement {
  override fun checkForce(force: List<ChosenVariant>): ForceRequirementMatch =
    ForceRequirementMatch.Full
}

private val List<ChosenVariant>.pvSum: Int
  get() = sumOf { it.unit.pointsValue }

class MaximizePV: ForceComparator {
  override fun compare(p0: List<ChosenVariant>?, p1: List<ChosenVariant>?): Int =
    compareValues(p0?.pvSum, p1?.pvSum)
}

class MaxPV(private val maxPv: Int): ForceRequirement {
  override fun checkForce(force: List<ChosenVariant>): ForceRequirementMatch =
    if (force.pvSum <= maxPv) ForceRequirementMatch.Full else ForceRequirementMatch.Forbidden
}

class MatchingTechBase(private val techBases: Set<TechBase>): ForceRequirement {
  override fun checkForce(force: List<ChosenVariant>): ForceRequirementMatch {
    val clanAllowed = techBases.contains(TechBase.Clan)
    val isAllowed = techBases.contains(TechBase.IS)
    val matches = force.all { if (it.unit.isClan) clanAllowed else isAllowed }
    return if (matches) ForceRequirementMatch.Full else ForceRequirementMatch.Forbidden
  }
}

class MatchesAllRequirements(private val requirements: List<ForceRequirement>):ForceRequirement {
  override fun checkForce(force: List<ChosenVariant>): ForceRequirementMatch =
    if (force.isEmpty()) {
      ForceRequirementMatch.Full
    } else {
      requirements.minOf { it.checkForce(force) }
    }
}

data class ForceChooser(
  val requirement: ForceRequirement,
  val comparator: ForceComparator,
) {
  fun chooseUnits(minis: List<Mini>, locked: Set<ChosenVariant>): List<ChosenVariant> {
    var current = locked.toList()
    val lockedMinis = locked.map { it.mini }.toSet()
    var available = (minis - lockedMinis).shuffled()

    // First, fill up the list with the highest value options
    while (true) {
      val next = available.firstNotNullOfOrNull { mini ->
        mini.possibleUnits.shuffled().firstNotNullOfOrNull { unit ->
          ChosenVariant(mini = mini, unit = unit).takeIf {
            requirement.checkForce(current + it) != ForceRequirementMatch.Forbidden
          }
        }
      }
      next?.also {
        current = current + it
        available = available - it.mini
      } ?: break
    }

    // TODO: Swap subsections to maximize value

    return current
  }
}

