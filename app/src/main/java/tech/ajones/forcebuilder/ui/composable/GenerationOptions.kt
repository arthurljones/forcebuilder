package tech.ajones.forcebuilder.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import tech.ajones.forcebuilder.MainActivityViewModel.MiniLibrary
import tech.ajones.forcebuilder.model.ForceSettings
import tech.ajones.forcebuilder.model.TechBase
import kotlin.math.roundToInt

@Composable
fun GenerationOptions(
  settingsSource: MutableStateFlow<ForceSettings>,
  onRandomizeTap: () -> Unit
) {
  Column {
    val settings by settingsSource.collectAsStateWithLifecycle()
    Text("Settings", style = MaterialTheme.typography.titleLarge)
    MultiChoiceSegmentedButtonRow {
      val items = MiniLibrary.entries
      items.forEachIndexed { index, library ->
        SegmentedButton(
          checked = settings.library == library,
          onCheckedChange = { settingsSource.update { it.copy(library = library) } },
          SegmentedButtonDefaults.itemShape(index = index, count = items.size),
        ) {
          Text(text = library.name)
        }
      }
    }
    MultiChoiceSegmentedButtonRow {
      val items = TechBase.entries
      items.forEachIndexed { index, tech ->
        SegmentedButton(
          checked = settings.techBase.contains(tech),
          onCheckedChange = { checked ->
            settingsSource.update {
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
    val max = 800
    val step = 10
    Slider(
      value = settings.maxPointsValue.toFloat(),
      onValueChange = { newPointsMax ->
        settingsSource.update { it.copy(maxPointsValue = newPointsMax.roundToInt()) }
      },
      steps = max / step - 1,
      valueRange = 0f..max.toFloat()
    )
    IntField(
      value = settings.maxPointsValue,
      label = { Text("Max PV") },
      onValueChange = { value ->
        settingsSource.update {
          it.copy(maxPointsValue = value ?: 0)
        }
      }
    )

    Row {
      IntField(
        value = settings.minUnits,
        label = { Text("Min Units") },
        modifier = Modifier.weight(1f),
        onValueChange = { newValue ->
          settingsSource.update { it.copy(minUnits = newValue) }
        },
      )
      Spacer(modifier = Modifier.size(4.dp))
      IntField(
        value = settings.maxUnits,
        label = { Text("Max Units") },
        modifier = Modifier.weight(1f),
        onValueChange = { newValue ->
          settingsSource.update { it.copy(maxUnits = newValue) }
        },
      )
    }

    Button(
      onClick = { onRandomizeTap() }
    ) {
      Text("Randomize")
    }
  }
}

@Preview
@Composable
private fun GenerationOptionsPreview() {
  PreviewContainer {
    GenerationOptions(
      settingsSource = MutableStateFlow(ForceSettings()),
      onRandomizeTap = { }
    )
  }
}
