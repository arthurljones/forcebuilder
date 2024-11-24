package tech.ajones.forcebuilder.model
interface ForceComparator: Comparator<List<MechInfo>>

sealed interface ForceRequirementMatch {
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
}

interface ForceRequirement {
  fun checkForce(force: List<MechInfo>): ForceRequirementMatch
}

class NoRequirement: ForceRequirement {
  override fun checkForce(force: List<MechInfo>): ForceRequirementMatch =
    ForceRequirementMatch.Full
}

private val List<MechInfo>.pvSum: Int
  get() = sumOf { it.pointsValue }

class MaximizePV: ForceComparator {
  override fun compare(p0: List<MechInfo>?, p1: List<MechInfo>?): Int =
    compareValues(p0?.pvSum, p1?.pvSum)
}

class MaxPV(val maxPv: Int): ForceRequirement {
  override fun checkForce(force: List<MechInfo>): ForceRequirementMatch =
    if (force.pvSum <= maxPv) ForceRequirementMatch.Full else ForceRequirementMatch.Forbidden
}

enum class PresortMode {
  /**
   * Uses input ordering
   */
  None,
  /**
   * Shuffles input
   */
  Random,
  /**
   * Sorts input by comparator in descending comparator value order
   */
  BestFirst,
  /**
   * Sorts input by comparator in ascending comparator value order
   */
  WorstFirst
}

data class ForceChooser(
  val requirement: ForceRequirement,
  val comparator: ForceComparator,
  val presortMode: PresortMode = PresortMode.None,
) {
  fun chooseMechs(mechs: List<MechInfo>): List<MechInfo> {
    // Maximize PV under the maximum
    var current = emptyList<MechInfo>()
    var available = when (presortMode) {
      PresortMode.None -> mechs
      PresortMode.Random -> mechs.shuffled()
      PresortMode.BestFirst -> sortByComparator(mechs).reversed()
      PresortMode.WorstFirst -> sortByComparator(mechs)
    }

    // First, fill up the list with the highest value options
    while (true) {
      val next = available.firstOrNull {
        requirement.checkForce(current + it) != ForceRequirementMatch.Forbidden
      }
      next?.also {
        current = current + it
        available = available - it
      } ?: break
    }

    // TODO: Swap subsections to maximize value

    return current
  }

  private fun sortByComparator(mechs: List<MechInfo>): List<MechInfo> =
    mechs
      .map { listOf(it) }
      .sortedWith(comparator)
      .map { it.first() }
}

