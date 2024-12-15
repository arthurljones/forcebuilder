package tech.ajones.forcebuilder.model

import kotlinx.coroutines.flow.MutableStateFlow
import tech.ajones.forcebuilder.MainActivityViewModel

enum class TechBase {
  IS, Clan
}

data class ForceSettings(
  val library: MainActivityViewModel.MiniLibrary = MainActivityViewModel.MiniLibrary.Tomas,
  val techBase: Set<TechBase> = TechBase.entries.toSet(),
  val maxPointsValue: Int = 300,
  val maxUnits: Int? = null,
  val minUnits: Int? = null,
  val availability: AvailabilityCriteria = AvailabilityCriteria(),
  /**
   * Units in this set will be in each generated force, even if they don't meet
   * the selected requirements
   */
  val lockedUnits: Set<ChosenVariant> = emptySet(),
  val priority: ForcePriority = MaximizePointsValue()
) {
  suspend fun chooseUnits(
    minis: List<Mini>,
    progress: MutableStateFlow<Float>? = null
  ): Set<ChosenVariant> {
    val scorer =ForceScorer(
      requirements = listOfNotNull(
        PointValueRange(max = maxPointsValue),
        MatchingTechBase(techBase),
        UnitCountRange(min = minUnits, max = maxUnits),
        AvailableInEra(criteria = availability),
        lockedUnits.takeIf { it.isNotEmpty() }?.let { IncludesUnits(it) }
      ),
      priority = priority
    )
    return ForceChooser.chooseUnits(
      scorer = scorer,
      allMinis = minis,
      initial = lockedUnits,
      progress = progress
    )
  }
}