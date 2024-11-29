package tech.ajones.forcebuilder.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import tech.ajones.forcebuilder.model.ChosenVariant

@Composable
fun UnitList(
  units: List<ChosenVariant>,
  lockedUnits: MutableStateFlow<Set<ChosenVariant>>,
) {
  Column {
    var showCards by remember { mutableStateOf(false) }
    val statsStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)
    Text("Count: ${units.size}", style = statsStyle)
    Text("PV: ${units.sumOf { it.unit.pointsValue }}", style = statsStyle)
    HorizontalDivider(modifier = Modifier.height(8.dp))
    Row {
      Checkbox(
        checked = showCards,
        onCheckedChange = { showCards = it }
      )
      Text(
        text = "Show AS Cards",
        modifier = Modifier.align(Alignment.CenterVertically)
      )
    }
    HorizontalDivider(modifier = Modifier.height(8.dp))
    units.forEach { unit ->
      UnitRow(
        unit = unit,
        lockedUnits = lockedUnits,
        showCards = showCards
      )
    }
  }
}

@Preview
@Composable
private fun UnitListPreview() {
  PreviewContainer {
    UnitList(
      units = previewUnits,
      lockedUnits = MutableStateFlow(emptySet())
    )
  }
}
