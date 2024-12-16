package tech.ajones.forcebuilder

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import tech.ajones.forcebuilder.model.AvailabilityCriteria
import tech.ajones.forcebuilder.model.ForceSettings
import tech.ajones.forcebuilder.model.ForceUnit
import tech.ajones.forcebuilder.model.LibraryMini
import tech.ajones.forcebuilder.model.LoadResult
import tech.ajones.forcebuilder.model.MiniLibrary
import tech.ajones.forcebuilder.model.OpenIntRange
import tech.ajones.forcebuilder.model.UnitSortField
import tech.ajones.forcebuilder.model.UnitSortOrder
import tech.ajones.forcebuilder.model.UnitVariant
import tech.ajones.forcebuilder.model.ajMiniNames
import tech.ajones.forcebuilder.model.tomasMiniNames
import tech.ajones.forcebuilder.model.updateSuccess
import tech.ajones.forcebuilder.ui.binder.ForceSettingsUpdater
import tech.ajones.forcebuilder.ui.binder.ForceUpdater
import tech.ajones.forcebuilder.ui.binder.UnitSortOrderUpdater
import java.util.concurrent.atomic.AtomicInteger

class MainActivityViewModel: ViewModel() {
  private val ajMinis: MutableStateFlow<List<LibraryMini>?> = MutableStateFlow(null)
  private val tomasMinis: MutableStateFlow<List<LibraryMini>?> = MutableStateFlow(null)

  val sortOrder: MutableStateFlow<UnitSortOrder<*, *>> =
    MutableStateFlow(
      UnitSortOrder(
        primary = UnitSortField.ByName,
        secondary = UnitSortField.ByName,
        ascending = true
      )
    )

  val forceSettings: MutableStateFlow<ForceSettings> = MutableStateFlow(ForceSettings())

  val allUnitsByChassis: MutableStateFlow<Map<String, List<UnitVariant>>?> =
    MutableStateFlow(null)

  private val availableMinis: StateFlow<List<LibraryMini>?> =
    combine(ajMinis, tomasMinis, forceSettings) { aj, tomas, settings ->
      when (settings.library) {
        MiniLibrary.AJ -> aj
        MiniLibrary.Tomas -> tomas
        MiniLibrary.Both -> (aj ?: emptyList()) + (tomas ?: emptyList())
      }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

  /**
   * The force that has been generated, if any
   */
  val generatedForce: MutableStateFlow<LoadResult<Set<ForceUnit>>?> =
    MutableStateFlow(null)

  private fun generateRandomForce() {
    // Cancel any ongoing generation
    (generatedForce.value as? LoadResult.CancelableLoading)
      ?.also { it.cancel() }

    viewModelScope.launch(Dispatchers.Default) {
      val outerContext = this
      generatedForce.value = LoadResult.CancelableLoading(cancel = { cancel() })
      val progress = MutableStateFlow(0f)
      val progressJob = launch {
        progress.collectLatest {
          if (isActive) {
            generatedForce.value = LoadResult.CancelableLoading(
              progress = it,
              cancel = {
                outerContext.cancel()
                generatedForce.value = null
              }
            )
          }
        }
      }

      val result = runCatching<Set<ForceUnit>?> {
        availableMinis.value
          ?.let { forceSettings.value.generateRandomForce(it, progress) } ?: run {
          cancel()
          null
        }
      }
      progressJob.cancel()
      progressJob.join()

      println(result)
      generatedForce.value = result.fold(
        onSuccess = { units -> units?.let { LoadResult.Success(it) } },
        onFailure = { LoadResult.Failure(it.message) }
      )
    }
  }

  fun setup(context: Context, allUnitsPath: String) {
    viewModelScope.launch(Dispatchers.IO) {
      val raw = context.assets.open(allUnitsPath).bufferedReader().use { it.readText() }
      val units = json.decodeFromString<List<UnitVariant>>(raw)
        .groupBy { it.preferredChassis }
        .also { allUnitsByChassis.value = it }

      tomasMinis.value = loadBakedLibrary(
        miniNames = tomasMiniNames,
        units = units
      )

      ajMinis.value = loadBakedLibrary(
        miniNames = ajMiniNames,
        units = units
      )
    }
  }

  val forceUpdater: ForceUpdater = object: ForceUpdater {
    override fun generateRandom() {
      generateRandomForce()
    }

    override fun addUnit(unit: ForceUnit) {
      generatedForce.updateSuccess { it + unit }
    }

    override fun replaceUnit(unit: ForceUnit, replacement: ForceUnit?) {
      val replacementList = listOfNotNull(replacement)
      forceSettings.update { settings ->
        // Only do replacement on locked units set if the original unit's mini is present
        settings.lockedUnits
          .firstOrNull { it.mini == unit.mini }
          ?.let { settings.copy(lockedUnits = settings.lockedUnits - it + replacementList) }
          ?: settings
      }
      generatedForce.updateSuccess { force ->
        force - unit + replacementList
      }
    }
  }

  val forceSettingsUpdater: ForceSettingsUpdater = object: ForceSettingsUpdater {
    override fun toggleUnitLocked(unit: ForceUnit) {
      forceSettings.update { settings ->
        val lockedUnits = settings.lockedUnits
        val unitLocked = lockedUnits.contains(unit)
        val newLockedUnits = if (unitLocked) {
          lockedUnits - unit
        } else {
          lockedUnits + unit
        }
        settings.copy(lockedUnits = newLockedUnits)
      }
    }

    override fun updateAvailability(update: (AvailabilityCriteria) -> AvailabilityCriteria) {
      forceSettings.update { settings ->
        settings.copy(availability = update(settings.availability))
      }
    }

    override fun updateLibrary(update: (MiniLibrary) -> MiniLibrary) {
      forceSettings.update { settings ->
        settings.copy(library = update(settings.library))
      }
    }

    override fun updateMaxPointsValue(update: (Int) -> Int) {
      forceSettings.update { settings ->
        settings.copy(maxPointsValue = update(settings.maxPointsValue))
      }
    }

    override fun updateUnitLimit(update: (OpenIntRange) -> OpenIntRange) {
      forceSettings.update { settings ->
        settings.copy(unitLimit = update(settings.unitLimit))
      }
    }

    override fun update(update: (ForceSettings) -> ForceSettings) {
      forceSettings.update(update)
    }
  }

  val unitSortOrderUpdater: UnitSortOrderUpdater = object: UnitSortOrderUpdater {
    override fun update(update: (UnitSortOrder<*, *>) -> UnitSortOrder<*, *>) {
      sortOrder.update(update)
    }
  }

  companion object {
    // TODO: Move this someplace better
    private val json = Json { ignoreUnknownKeys = true }
    // TODO: This should be a database row id
    private val miniIdGenerator = AtomicInteger()

    private fun loadBakedLibrary(
      miniNames: List<String>,
      units: Map<String, List<UnitVariant>>
    ): List <LibraryMini> =
      miniNames.map { chassis ->
        val chassisUnits = units[chassis]
          ?: run {
            Log.w("", "no units found for mini chassis '$chassis'")
            emptyList()
          }
        LibraryMini(
          chassis = chassis,
          variants = chassisUnits,
          id = miniIdGenerator.getAndIncrement()
        )
      }
  }
}

