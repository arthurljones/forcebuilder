package tech.ajones.forcebuilder.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import tech.ajones.forcebuilder.model.ChosenVariant

@Composable
fun ForceScreen(
  units: List<ChosenVariant>?,
  maxPvSource: MutableStateFlow<Int>,
  lockedUnits: MutableStateFlow<Set<ChosenVariant>>,
  onRandomizeTap: () -> Unit
) {
  Column {
    GenerationOptions(
      maxPvSource = maxPvSource,
      onRandomizeTap = onRandomizeTap
    )

    HorizontalDivider(Modifier.padding(vertical = 16.dp))
    Text("Force", style = MaterialTheme.typography.titleLarge)

    units?.also { units ->
      UnitList(
        units = units,
        lockedUnits = lockedUnits,
      )

      HorizontalDivider(Modifier.padding(vertical = 16.dp))

      ListActions(units)
    } ?: run {
      Text("No units selected")
    }
  }
}

@Preview(device = Devices.PIXEL_7)
@Composable
private fun ForceScreenPreview() {
  PreviewContainer {
    Box(modifier = Modifier
      .padding(16.dp)
      .verticalScroll(rememberScrollState())
    ) {
      ForceScreen(
        units = previewUnits,
        lockedUnits = MutableStateFlow(emptySet()),
        maxPvSource = MutableStateFlow(300),
        onRandomizeTap = { }
      )
    }
  }
}