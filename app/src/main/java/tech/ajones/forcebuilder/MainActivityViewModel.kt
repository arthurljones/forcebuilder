package tech.ajones.forcebuilder

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import tech.ajones.forcebuilder.model.ChosenVariant
import tech.ajones.forcebuilder.model.ForceChooser
import tech.ajones.forcebuilder.model.ForceSettings
import tech.ajones.forcebuilder.model.MatchesAllRequirements
import tech.ajones.forcebuilder.model.MatchingTechBase
import tech.ajones.forcebuilder.model.MaxPV
import tech.ajones.forcebuilder.model.MaximizePV
import tech.ajones.forcebuilder.model.Mini
import tech.ajones.forcebuilder.model.UnitVariant
import tech.ajones.forcebuilder.model.ajMiniNames
import tech.ajones.forcebuilder.model.tomasMiniNames

class MainActivityViewModel: ViewModel() {
  private val ajMinis: MutableStateFlow<List<Mini>?> = MutableStateFlow(null)
  private val tomasMinis: MutableStateFlow<List<Mini>?> = MutableStateFlow(null)

  enum class MiniLibrary {
    AJ, Tomas, Both
  }

  val forceSettings: MutableStateFlow<ForceSettings> = MutableStateFlow(ForceSettings())

  val allUnitsByChassis: MutableStateFlow<Map<String, List<UnitVariant>>?> =
    MutableStateFlow(null)

  private val availableMinis: StateFlow<List<Mini>?> =
    combine(ajMinis, tomasMinis, forceSettings) { aj, tomas, settings ->
      when (settings.library) {
        MiniLibrary.AJ -> aj
        MiniLibrary.Tomas -> tomas
        MiniLibrary.Both -> (aj ?: emptyList()) + (tomas ?: emptyList())
      }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

  private val randomizeCount: MutableStateFlow<Int> = MutableStateFlow(0)

  /**
   * Units in this set will be in each generated force, even if they don't meet
   * the selected requirements
   */
  val lockedUnits: MutableStateFlow<Set<ChosenVariant>> = MutableStateFlow(emptySet())

  val chosen: StateFlow<List<ChosenVariant>?> =
    combine(
      availableMinis,
      forceSettings,
      randomizeCount
    ) { available, settings, _ ->
      available?.let {
        ForceChooser(
          requirement = MatchesAllRequirements(listOf(
            MaxPV(settings.maxPointsValue),
            MatchingTechBase(settings.techBase)
          )),
          comparator = MaximizePV(),
          // We don't include `lockedUnits` in the `combine` call above because
          // we don't want to regenerate when it changes.
        ).chooseUnits(minis = it, locked = lockedUnits.value)
      }
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

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

  fun onRandomizeTap() {
    randomizeCount.update { it + 1 }
  }

  companion object {
    // TODO: Move this someplace better
    private val json = Json { ignoreUnknownKeys = true }

    private fun loadBakedLibrary(
      miniNames: List<String>,
      units: Map<String, List<UnitVariant>>
    ): List <Mini> {
      val rawMinis = miniNames
        .groupingBy { it }
        .eachCount()

      return rawMinis.map { (chassis, count) ->
        val chassisUnits = units[chassis]
          ?: run {
            Log.w("", "no units found for mini chassis '$chassis'")
            emptyList()
          }
        Mini(
          chassis = chassis,
          count = count,
          possibleUnits = chassisUnits
        )
      }
    }
  }
}

