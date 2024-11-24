package tech.ajones.forcebuilder

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import tech.ajones.forcebuilder.model.ForceChooser
import tech.ajones.forcebuilder.model.MaxPV
import tech.ajones.forcebuilder.model.MaximizePV
import tech.ajones.forcebuilder.model.PresortMode
import tech.ajones.forcebuilder.model.UnitInfo

class MainActivityViewModel: ViewModel() {
  val availableMechs: MutableStateFlow<List<UnitInfo>?> = MutableStateFlow(null)
  val maxPointValue: MutableStateFlow<Int> = MutableStateFlow(300)
  private val randomizeCount: MutableStateFlow<Int> = MutableStateFlow(0)

  /**
   * Units in this set will be in each generated force, even if they don't meet
   * the selected requirements
   */
  val lockedUnits: MutableStateFlow<Set<UnitInfo>> = MutableStateFlow(emptySet())

  val result: StateFlow<List<UnitInfo>?> =
    combine(
      availableMechs,
      maxPointValue,
      randomizeCount
    ) { available, maxPv, _ ->
      available?.let {
        ForceChooser(
          requirement = MaxPV(maxPv),
          comparator = MaximizePV(),
          presortMode = PresortMode.Random
          // We don't include `lockedUnits` in the `combine` call above because
          // we don't want to regenerate when it changes.
        ).chooseUnits(units = it, forced = lockedUnits.value)
      }
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

  fun setupFromCsvAsset(context: Context, assetPath: String) {
    val raw = csvReader().readAll(context.assets.open(assetPath))
    println(raw)
    availableMechs.value = raw
      .drop(1)
      .map { (name, variant1, pv1, role1, variant2, pv2, role2) ->
        listOfNotNull(
          variant1.takeIf { it.isNotBlank() }?.let { it to pv1 },
          variant2.takeIf { it.isNotBlank() }?.let { it to pv2 }
        ).map { (variant, pv) ->
          UnitInfo(chassis = name, variant = variant, pointsValue = pv.toInt())
        }
      }.flatten()
  }

  fun onRandomizeTap() {
    randomizeCount.update { it + 1 }
  }
}

