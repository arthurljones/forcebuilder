package tech.ajones.forcebuilder.model

import android.util.Range
import kotlinx.coroutines.flow.MutableStateFlow

enum class TechBase {
  IS, Clan
}

data class OpenIntRange(
  val min: Int? = null,
  val max: Int? = null
)

data class ForceSettings(
  val library: MiniLibrary = MiniLibrary.Tomas,
  val techBase: Set<TechBase> = TechBase.entries.toSet(),
  val maxPointsValue: Int = 300,
  val unitLimit: OpenIntRange = OpenIntRange(),
  val maxUnits: Int? = unitLimit.max,
  val minUnits: Int? = unitLimit.min,
  val availability: AvailabilityCriteria = AvailabilityCriteria(),
  /**
   * Units in this set will be in each generated force, even if they don't meet
   * the selected requirements
   */
  val lockedUnits: Set<ForceUnit> = emptySet(),
  val defaultSkill: Int = 4,
  val priority: ForcePriority = MaximizePointsValue()
) {
  val scorer: ForceScorer by lazy {
    ForceScorer(
      requirements = listOfNotNull(
        PointValueRange(max = maxPointsValue),
        MatchingTechBase(techBase),
        UnitCountRange(unitLimit),
        AvailableInEra(criteria = availability),
        lockedUnits.takeIf { it.isNotEmpty() }?.let { IncludesUnits(it) }
      ),
      priority = priority
    )
  }

  suspend fun generateRandomForce(
    minis: List<LibraryMini>,
    progress: MutableStateFlow<Float>? = null
  ): Set<ForceUnit> {
    return ForceChooser.chooseUnits(
      scorer = scorer,
      allMinis = minis,
      initial = lockedUnits,
      progress = progress
    )
  }
}