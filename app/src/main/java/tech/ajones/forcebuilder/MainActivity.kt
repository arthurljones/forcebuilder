package tech.ajones.forcebuilder

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.update
import kotlin.math.roundToInt

class MainActivity: ComponentActivity() {
  private val model: MainActivityViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    model.setupFromCsvAsset(context = this, assetPath = "inventory-tomas.csv")

    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val snackbarHostState = remember { SnackbarHostState() }
      MaterialTheme {
        Scaffold(
          snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->
          Column(modifier = Modifier
            .padding(padding)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())) {
            MainScreen(snackbarHostState)
          }
        }
      }
    }
  }

  @Composable
  private fun ColumnScope.MainScreen(
    snackbarHostState: SnackbarHostState
  ) {
    val scope = rememberCoroutineScope()
    Text("Settings", style = MaterialTheme.typography.titleMedium)
    val maxPv by model.maxPointValue.collectAsStateWithLifecycle()
    val max = 800
    val step = 10
    Slider(
      value = maxPv.toFloat(),
      onValueChange = { model.maxPointValue.value = it.roundToInt() },
      steps = max / step - 1,
      valueRange = 0f..max.toFloat()
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
    Text("Force", style = MaterialTheme.typography.titleMedium)

    val locked by model.lockedUnits.collectAsStateWithLifecycle()
    val results by model.result.collectAsStateWithLifecycle()
    results?.also { units ->
      val statsStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)
      Text("Count: ${units.size}", style = statsStyle)
      Text("PV: ${units.sumOf { it.pointsValue }}", style = statsStyle)
      Spacer(modifier = Modifier.height(8.dp))
      units.forEach { unit ->
        Row(modifier = Modifier.height(32.dp)
          .clickable {
            model.lockedUnits.update { if (it.contains(unit)) it - unit else it + unit }
          }
        ) {
          Checkbox(
            checked = locked.contains(unit),
            onCheckedChange = { checked ->
              model.lockedUnits.update { if (checked) it + unit else it - unit }
            }
          )
          Text(
            text = unit.toString(),
            modifier = Modifier.align(Alignment.CenterVertically)
          )
        }
      }
    } ?: run {
      Text("No results")
    }

    HorizontalDivider(Modifier.padding(vertical = 16.dp))
    Text("Actions", style = MaterialTheme.typography.titleMedium)

    Row(
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
      val text = results?.joinToString("\n") ?: ""
      val clipboard = LocalClipboardManager.current
      Button(
        enabled = results?.isNotEmpty() == true,
        onClick = { clipboard.setText(AnnotatedString(text)) }
      ) {
        Text("Copy To Clipboard")
      }

      val context = LocalContext.current
      Button(
        onClick = {
          val sendIntent: Intent = Intent().also {
            it.action = Intent.ACTION_SEND
            it.putExtra(Intent.EXTRA_TEXT, text)
            it.type = "text/plain"
          }
          val shareIntent = Intent.createChooser(sendIntent, null)
          context.startActivity(shareIntent)
        }
      ){
        Text("Share")
      }
    }
  }
}
