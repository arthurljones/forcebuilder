package tech.ajones.forcebuilder

import android.os.Bundle
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

class MainActivity: ComponentActivity() {
  private val model: MainActivityViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    model.setupFromCsvAsset(context = this, assetPath = "inventory-tomas.csv")

    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
          Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
            val results by model.result.collectAsStateWithLifecycle()
            val maxPv by model.maxPointValue.collectAsStateWithLifecycle()
            Slider(
              value = maxPv.toFloat(),
              onValueChange = { model.maxPointValue.value = it.toInt() },
              valueRange = 0f..800f
            )
            val pattern = remember { Regex("""^(?:\d+)?$""") }
            TextField(
              value = maxPv.toString(),
              label = { Text("Max PV") },
              onValueChange = {
                if (it.matches(pattern)) {
                  model.maxPointValue.value = try {
                    it.toInt()
                  } catch (ex: NumberFormatException) {
                    0
                  }
                }
              },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Button(
              onClick = { model.onRandomizeTap() }
            ) {
              Text("Randomize")
            }

            HorizontalDivider(Modifier.padding(vertical = 16.dp))

            results?.also { mechs ->
              Text("Force PV: ${mechs.sumOf { it.pointsValue }}")
              Text("Force:")
              mechs.forEach {
                Text("${it.chassis} ${it.variant} (PV: ${it.pointsValue})")
              }
            } ?: run {
              Text("No results")
            }
          }
        }
      }
    }
  }
}
