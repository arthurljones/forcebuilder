package tech.ajones.forcebuilder.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import kotlin.math.roundToInt

@Composable
fun GenerationOptions(
  maxPvSource: MutableStateFlow<Int>,
  onRandomizeTap: () -> Unit
) {
  Column {
    Text("Settings", style = MaterialTheme.typography.titleLarge)
    val maxPv by maxPvSource.collectAsStateWithLifecycle()
    val max = 800
    val step = 10
    Slider(
      value = maxPv.toFloat(),
      onValueChange = { maxPvSource.value = it.roundToInt() },
      steps = max / step - 1,
      valueRange = 0f..max.toFloat()
    )
    val pattern = remember { Regex("""^(?:\d+)?$""") }
    TextField(
      value = maxPv.toString(),
      label = { Text("Max PV") },
      onValueChange = {
        if (it.matches(pattern)) {
          maxPvSource.value = try {
            it.toInt()
          } catch (ex: NumberFormatException) {
            0
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
      maxPvSource = MutableStateFlow(300),
      onRandomizeTap = { }
    )
  }
}
