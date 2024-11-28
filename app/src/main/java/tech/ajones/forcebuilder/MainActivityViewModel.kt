package tech.ajones.forcebuilder

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.serialization.json.Json
import tech.ajones.forcebuilder.model.ChosenVariant
import tech.ajones.forcebuilder.model.ForceChooser
import tech.ajones.forcebuilder.model.MaxPV
import tech.ajones.forcebuilder.model.MaximizePV
import tech.ajones.forcebuilder.model.Mini
import tech.ajones.forcebuilder.model.UnitVariant

class MainActivityViewModel: ViewModel() {
  data class AvailableInfo(
    val allUnitsByChassis: Map<String, List<UnitVariant>>,
    val availableMinis: List<Mini>,
  )

  private val availableInfo: MutableStateFlow<AvailableInfo?> = MutableStateFlow(null)
  val maxPointValue: MutableStateFlow<Int> = MutableStateFlow(300)
  private val randomizeCount: MutableStateFlow<Int> = MutableStateFlow(0)

  /**
   * Units in this set will be in each generated force, even if they don't meet
   * the selected requirements
   */
  val lockedUnits: MutableStateFlow<Set<ChosenVariant>> = MutableStateFlow(emptySet())

  val chosen: StateFlow<List<ChosenVariant>?> =
    combine(
      availableInfo,
      maxPointValue,
      randomizeCount
    ) { available, maxPv, _ ->
      available?.let {
        ForceChooser(
          requirement = MaxPV(maxPv),
          comparator = MaximizePV(),
          // We don't include `lockedUnits` in the `combine` call above because
          // we don't want to regenerate when it changes.
        ).chooseUnits(minis = it.availableMinis, locked = lockedUnits.value)
      }
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

  fun setupFromCsvAsset(context: Context, minisPath: String, allUnitsPath: String) {
    viewModelScope.launch(Dispatchers.IO) {
      supervisorScope {
        val deferredMinis = async {
          context.assets.open(minisPath)
            .bufferedReader()
            .use { it.readLines() }
            .drop(1)
            .groupingBy { it }
            .eachCount()
        }

        val deferredUnits = async {
          val raw = context.assets.open(allUnitsPath).bufferedReader().use { it.readText() }
          val units = json.decodeFromString<List<UnitVariant>>(raw)
          units.groupBy { it.preferredChassis }
        }

        val rawMinis = deferredMinis.await()
        val units = deferredUnits.await()

        val minis = rawMinis.map { (chassis, count) ->
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

        availableInfo.value = AvailableInfo(
          allUnitsByChassis = units,
          availableMinis = minis
        )
      }
    }
  }

  fun onRandomizeTap() {
    randomizeCount.update { it + 1 }
  }

  companion object {
    // TODO: Move this someplace better
    private val json = Json { ignoreUnknownKeys = true }
  }
}

