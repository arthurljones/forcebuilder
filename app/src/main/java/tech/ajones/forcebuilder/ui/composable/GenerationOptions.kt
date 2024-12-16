package tech.ajones.forcebuilder.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tech.ajones.forcebuilder.model.MiniLibrary
import tech.ajones.forcebuilder.model.AvailableTechLevel
import tech.ajones.forcebuilder.model.Era
import tech.ajones.forcebuilder.model.ForceSettings
import tech.ajones.forcebuilder.model.TechBase
import tech.ajones.forcebuilder.ui.binder.ForceSettingsUpdater
import tech.ajones.forcebuilder.ui.binder.ForceUpdater
import kotlin.math.roundToInt

@Composable
fun GenerationOptions(
  forceUpdater: ForceUpdater,
  settingsSource: StateFlow<ForceSettings>,
  settingsUpdater: ForceSettingsUpdater,
) {
  Column {
    Text("Settings", style = MaterialTheme.typography.titleLarge)

    LibrarySelector(settingsSource = settingsSource, settingsUpdater = settingsUpdater)
    TechBaseSelector(settingsSource = settingsSource, settingsUpdater = settingsUpdater)
    AvailabilitySelector(settingsSource = settingsSource, settingsUpdater = settingsUpdater)
    MaxPVSelector(settingsSource = settingsSource, settingsUpdater = settingsUpdater)
    MinMaxUnitsSelector(settingsSource = settingsSource, settingsUpdater = settingsUpdater)

    Button(
      onClick = { forceUpdater.generateRandom() }
    ) {
      Text("Randomize")
    }
  }
}

@Composable
private fun LibrarySelector(
  settingsSource: StateFlow<ForceSettings>,
  settingsUpdater: ForceSettingsUpdater,
) {
  val settings by settingsSource.collectAsStateWithLifecycle()
  MultiChoiceSegmentedButtonRow {
    val items = MiniLibrary.entries
    items.forEachIndexed { index, library ->
      SegmentedButton(
        checked = settings.library == library,
        onCheckedChange = { settingsUpdater.updateLibrary { library } },
        SegmentedButtonDefaults.itemShape(index = index, count = items.size),
      ) {
        Text(text = library.name)
      }
    }
  }
}

@Composable
private fun TechBaseSelector(
  settingsSource: StateFlow<ForceSettings>,
  settingsUpdater: ForceSettingsUpdater,
) {
  val settings by settingsSource.collectAsStateWithLifecycle()
  MultiChoiceSegmentedButtonRow {
    val items = TechBase.entries
    items.forEachIndexed { index, tech ->
      SegmentedButton(
        checked = settings.techBase.contains(tech),
        onCheckedChange = { checked ->
          settingsUpdater.update {
            val newVal = if (checked) it.techBase + tech else it.techBase - tech
            it.copy(techBase = newVal)
          }
        },
        SegmentedButtonDefaults.itemShape(index = index, count = items.size),
      ) {
        Text(text = tech.name)
      }
    }
  }
}

@Composable
private fun AvailabilitySelector(
  settingsSource: StateFlow<ForceSettings>,
  settingsUpdater: ForceSettingsUpdater,
) {
  val settings by settingsSource.collectAsStateWithLifecycle()
  val availability = settings.availability
  val eras = Era.entries
  val minEra = availability.minEra
  val maxEra = availability.maxEra
  RangeSlider(
    value = minEra.ordinal.toFloat()..maxEra.ordinal.toFloat(),
    onValueChange = { range ->
      settingsUpdater.updateAvailability {
        it.copy(
          minEra = eras[range.start.roundToInt()],
          maxEra = eras[range.endInclusive.roundToInt()]
        )
      }
    },
    steps = eras.size-2,
    valueRange = 0f..eras.lastIndex.toFloat()
  )
  val labelText = if (minEra == maxEra) {
    minEra.name
  } else {
    "${minEra.name} to ${maxEra.name}"
  }
  Text(text = "Era: $labelText")
  SingleChoiceSegmentedButtonRow {
    val items = AvailableTechLevel.entries
    items.forEachIndexed { index, item ->
      SegmentedButton(
        selected = availability.level == item,
        onClick = {
          settingsUpdater.updateAvailability { it.copy(level = item) }
        },
        shape = SegmentedButtonDefaults.itemShape(index = index, count = items.size),
      ) {
        Text(text = item.name)
      }
    }
  }
}

@Composable
private fun MaxPVSelector(
  settingsSource: StateFlow<ForceSettings>,
  settingsUpdater: ForceSettingsUpdater,
) {
  val settings by settingsSource.collectAsStateWithLifecycle()
  val max = 800
  val step = 10
  Slider(
    value = settings.maxPointsValue.toFloat(),
    onValueChange = { newPointsMax ->
      settingsUpdater.updateMaxPointsValue { newPointsMax.roundToInt() }
    },
    steps = max / step - 1,
    valueRange = 0f..max.toFloat()
  )
  IntField(
    value = settings.maxPointsValue,
    label = { Text("Max PV") },
    onValueChange = { value ->
      settingsUpdater.updateMaxPointsValue { value ?: 0 }
    }
  )
}

@Composable
private fun MinMaxUnitsSelector(
  settingsSource: StateFlow<ForceSettings>,
  settingsUpdater: ForceSettingsUpdater,
) {
  val settings by settingsSource.collectAsStateWithLifecycle()
  Row {
    IntField(
      value = settings.minUnits,
      label = { Text("Min Units") },
      modifier = Modifier.weight(1f),
      onValueChange = { newValue ->
        settingsUpdater.updateUnitLimit { it.copy(min = newValue) }
      },
    )
    Spacer(modifier = Modifier.size(4.dp))
    IntField(
      value = settings.unitLimit.max,
      label = { Text("Max Units") },
      modifier = Modifier.weight(1f),
      onValueChange = { newValue ->
        settingsUpdater.updateUnitLimit { it.copy(max = newValue) }
      },
    )
  }
}

@Preview
@Composable
private fun GenerationOptionsPreview() {
  PreviewContainer {
    GenerationOptions(
      forceUpdater = ForceUpdater.stub,
      settingsSource = MutableStateFlow(ForceSettings()),
      settingsUpdater = ForceSettingsUpdater.stub,
    )
  }
}
