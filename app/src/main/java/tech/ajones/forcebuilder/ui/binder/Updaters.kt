package tech.ajones.forcebuilder.ui.binder

import tech.ajones.forcebuilder.model.AvailabilityCriteria
import tech.ajones.forcebuilder.model.ForceSettings
import tech.ajones.forcebuilder.model.ForceUnit
import tech.ajones.forcebuilder.model.MiniLibrary
import tech.ajones.forcebuilder.model.OpenIntRange
import tech.ajones.forcebuilder.model.UnitSortOrder

interface ForceSettingsUpdater {
  fun toggleUnitLocked(unit: ForceUnit)
  fun updateAvailability(update: (AvailabilityCriteria) -> AvailabilityCriteria)
  fun updateLibrary(update: (MiniLibrary) -> MiniLibrary)
  fun updateMaxPointsValue(update: (Int) -> Int)
  fun updateUnitLimit(update: (OpenIntRange) -> OpenIntRange)

  fun update(update: (ForceSettings) -> ForceSettings)

  companion object {
    val stub: ForceSettingsUpdater = object: ForceSettingsUpdater {
      override fun toggleUnitLocked(unit: ForceUnit) { }
      override fun updateAvailability(update: (AvailabilityCriteria) -> AvailabilityCriteria) { }
      override fun updateLibrary(update: (MiniLibrary) -> MiniLibrary) { }
      override fun updateMaxPointsValue(update: (Int) -> Int) { }
      override fun updateUnitLimit(update: (OpenIntRange) -> OpenIntRange) { }

      override fun update(update: (ForceSettings) -> ForceSettings) { }
    }
  }
}

interface ForceUpdater {
  fun generateRandom()
  fun addUnit(unit: ForceUnit)
  fun replaceUnit(unit: ForceUnit, replacement: ForceUnit?)

  companion object {
    val stub: ForceUpdater = object: ForceUpdater {
      override fun generateRandom() { }
      override fun addUnit(unit: ForceUnit) { }
      override fun replaceUnit(unit: ForceUnit, replacement: ForceUnit?) { }
    }
  }
}

interface UnitSortOrderUpdater {
  fun update(update: (UnitSortOrder<*,*>) -> UnitSortOrder<*,*>)
  companion object {
    val stub: UnitSortOrderUpdater = object: UnitSortOrderUpdater {
      override fun update(update: (UnitSortOrder<*,*>) -> UnitSortOrder<*,*>) { }
    }
  }
}
