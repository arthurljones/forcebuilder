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
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import tech.ajones.forcebuilder.model.ChosenVariant
import tech.ajones.forcebuilder.model.ForceChooser
import tech.ajones.forcebuilder.model.ForceScorer
import tech.ajones.forcebuilder.model.ForceSettings
import tech.ajones.forcebuilder.model.IncludesUnits
import tech.ajones.forcebuilder.model.LoadResult
import tech.ajones.forcebuilder.model.MatchingTechBase
import tech.ajones.forcebuilder.model.MaximizePointsValue
import tech.ajones.forcebuilder.model.Mini
import tech.ajones.forcebuilder.model.PointValueRange
import tech.ajones.forcebuilder.model.UnitCountRange
import tech.ajones.forcebuilder.model.UnitVariant
import tech.ajones.forcebuilder.model.ajMiniNames
import tech.ajones.forcebuilder.model.tomasMiniNames
import java.util.concurrent.atomic.AtomicInteger

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

  /**
   * Units in this set will be in each generated force, even if they don't meet
   * the selected requirements
   */
  val lockedUnits: MutableStateFlow<Set<ChosenVariant>> = MutableStateFlow(emptySet())

  /**
   * The force that has been generated, if any
   */
  val generatedForce: MutableStateFlow<LoadResult<Set<ChosenVariant>>?> =
    MutableStateFlow(null)

  fun generateRandomForce() {
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

      val result = runCatching<Set<ChosenVariant>?> {
        val minis = availableMinis.value ?: run {
          cancel()
          return@runCatching null
        }
        val settings = forceSettings.value
        val locked = lockedUnits.value.toSet()
        val scorer = ForceScorer(
          requirements = listOfNotNull(
            PointValueRange(max = settings.maxPointsValue),
            MatchingTechBase(settings.techBase),
            UnitCountRange(min = settings.minUnits, max = settings.maxUnits),
            locked.takeIf { it.isNotEmpty() }?.let { IncludesUnits(it) }
          ),
          priority = MaximizePointsValue()
        )
        return@runCatching ForceChooser.chooseUnits(
          scorer = scorer,
          allMinis = minis,
          initial = locked,
          progress = progress
        )
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

  companion object {
    // TODO: Move this someplace better
    private val json = Json { ignoreUnknownKeys = true }
    // TODO: This should be a database row id
    private val miniIdGenerator = AtomicInteger()

    private fun loadBakedLibrary(
      miniNames: List<String>,
      units: Map<String, List<UnitVariant>>
    ): List <Mini> =
      miniNames.map { chassis ->
        val chassisUnits = units[chassis]
          ?: run {
            Log.w("", "no units found for mini chassis '$chassis'")
            emptyList()
          }
        Mini(
          chassis = chassis,
          possibleUnits = chassisUnits,
          id = miniIdGenerator.getAndIncrement()
        )
      }
  }
}

