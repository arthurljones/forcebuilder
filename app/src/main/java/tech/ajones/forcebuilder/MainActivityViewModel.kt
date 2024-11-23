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

operator fun <T> List<T>.component6(): T = get(5)
operator fun <T> List<T>.component7(): T = get(6)

class MainActivityViewModel: ViewModel() {
  val availableMechs: MutableStateFlow<List<MechInfo>?> = MutableStateFlow(null)
  val maxPointValue: MutableStateFlow<Int> = MutableStateFlow(300)

  val result: StateFlow<List<MechInfo>?> =
    combine(
      availableMechs,
      maxPointValue
    ) { available, maxPv ->
      available?.also {
        val chooser = SimpleForceChooser(
          criteria = MaxPV(maxPv),
          comparator = MaximizePV()
        )
        chooseMechs(it, chooser)
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
          MechInfo(chassis = name, variant = variant, pointsValue = pv.toInt())
        }
      }.flatten()
  }
}

data class MechInfo(
  val chassis: String,
  val variant: String,
  val pointsValue: Int
)

interface ForceComparator: Comparator<List<MechInfo>>

interface ForceRequirement {
  fun meetsRequirement(force: List<MechInfo>): Boolean
}

private val List<MechInfo>.pvSum: Int
  get() = sumOf { it.pointsValue }

class MaximizePV: ForceComparator {
  override fun compare(p0: List<MechInfo>?, p1: List<MechInfo>?): Int =
    compareValues(p0?.pvSum, p1?.pvSum)
}

class MaxPV(val maxPv: Int): ForceRequirement {
  override fun meetsRequirement(force: List<MechInfo>): Boolean = force.pvSum <= maxPv
}

class SimpleForceChooser(
  val criteria: ForceRequirement,
  val comparator: ForceComparator
): ForceRequirement by criteria, ForceComparator by comparator

private fun <T> chooseMechs(mechs: List<MechInfo>, chooser: T): List<MechInfo>
        where T: ForceComparator, T: ForceRequirement {
  // Maximize PV under the maximum
  var current = emptyList<MechInfo>()
  // Sort available by chooser to approximate best first
  var available = mechs
    .map { listOf(it) }
    .sortedWith(chooser)
    .map { it.first() }

  // First, fill up the list with the highest value options
  while (true) {
    val next = available.firstOrNull {
      chooser.meetsRequirement(current + it)
    }
    next?.also {
      current = current + it
      available = available - it
    } ?: break
  }

  // TODO: Swap subsections to maximize value

  return current
}

