package tech.ajones.forcebuilder.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import tech.ajones.forcebuilder.MainActivityViewModel.MiniLibrary
import tech.ajones.forcebuilder.model.ForceSettings
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
      MiniLibrary.entries.forEachIndexed { index, library ->
        SegmentedButton(
          checked = settings.library == library,
          onCheckedChange = { settingsSource.update { it.copy(library = library) } },
          SegmentedButtonDefaults.itemShape(index = index, count = MiniLibrary.entries.size),
        ) {
          Text(text = library.name)
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
    val pattern = remember { Regex("""^(?:\d+)?$""") }
    TextField(
      value = settings.maxPointsValue.toString(),
      label = { Text("Max PV") },
      onValueChange = { value ->
        if (value.matches(pattern)) {
          val newPointsMax = try {
            value.toInt()
          } catch (ex: NumberFormatException) {
            0
          }
          settingsSource.update {
            it.copy(maxPointsValue = newPointsMax)
          }
        }
      },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

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
