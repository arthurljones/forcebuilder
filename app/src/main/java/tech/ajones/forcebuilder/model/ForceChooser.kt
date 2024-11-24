package tech.ajones.forcebuilder.model
interface ForceComparator: Comparator<List<UnitInfo>>

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
  fun checkForce(force: List<UnitInfo>): ForceRequirementMatch
}

class NoRequirement: ForceRequirement {
  override fun checkForce(force: List<UnitInfo>): ForceRequirementMatch =
    ForceRequirementMatch.Full
}

private val List<UnitInfo>.pvSum: Int
  get() = sumOf { it.pointsValue }

class MaximizePV: ForceComparator {
  override fun compare(p0: List<UnitInfo>?, p1: List<UnitInfo>?): Int =
    compareValues(p0?.pvSum, p1?.pvSum)
}

class MaxPV(val maxPv: Int): ForceRequirement {
  override fun checkForce(force: List<UnitInfo>): ForceRequirementMatch =
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
  fun chooseUnits(units: List<UnitInfo>, forced: Set<UnitInfo>): List<UnitInfo> {
    var current = forced.toList()
    val starting = units - forced
    var available = when (presortMode) {
      PresortMode.None -> starting
      PresortMode.Random -> starting.shuffled()
      PresortMode.BestFirst -> sortByComparator(starting).reversed()
      PresortMode.WorstFirst -> sortByComparator(starting)
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

  private fun sortByComparator(units: List<UnitInfo>): List<UnitInfo> =
    units
      .map { listOf(it) }
      .sortedWith(comparator)
      .map { it.first() }
}

