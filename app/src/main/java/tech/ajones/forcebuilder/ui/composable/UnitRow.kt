package tech.ajones.forcebuilder.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import tech.ajones.forcebuilder.model.ChosenVariant
import tech.ajones.forcebuilder.model.ForceSettings
import tech.ajones.forcebuilder.toggle
import tech.ajones.forcebuilder.update

@Composable
fun UnitRow(
  unit: ChosenVariant,
  settingsSource: MutableStateFlow<ForceSettings>,
  expandedUnitsState: MutableState<Set<ChosenVariant>>,
  showCards: Boolean
) {
  Column(modifier = Modifier
    .fillMaxWidth()
    .clickable { expandedUnitsState.update { it.toggle(unit) } }
  ) {
    Row {
      val settings by settingsSource.collectAsStateWithLifecycle()
      val lockIconModifier = Modifier.size(18.dp).align(Alignment.CenterVertically)
      if (settings.lockedUnits.contains(unit)) {
        Icon(
          imageVector = Icons.Default.Lock,
          contentDescription = "Unit locked",
          modifier = lockIconModifier,
        )
      } else {
        Spacer(
          modifier = lockIconModifier,
        )
      }
      Text(text = unit.toString())
    }
    val variant = unit.unit
    if (expandedUnitsState.value.contains(unit)) {
      UnitInfo(
        unit = unit,
        showMulCard = showCards,
        settingsSource = settingsSource,
      )
    } else {
      SimpleUnitInfo(variant)
    }
    HorizontalDivider()
  }
}

@Preview
@Composable
private fun UnitRowPreview() {
  val unit = previewUnits.first()
  PreviewContainer {
    val expanded = remember { mutableStateOf<Set<ChosenVariant>>(emptySet()) }
    UnitRow(
      unit = unit,
      settingsSource = MutableStateFlow(ForceSettings()),
      expandedUnitsState = expanded,
      showCards = false
    )
  }
}

@Preview
@Composable
private fun UnitRowPreviewWithInfo() {
  PreviewContainer {
    val unit = previewUnits.first()
    val expanded = remember { mutableStateOf(setOf(unit)) }
    UnitRow(
      unit = unit,
      settingsSource = MutableStateFlow(ForceSettings()),
      expandedUnitsState = expanded,
      showCards = false
    )
  }
}

@Preview
@Composable
private fun UnitRowPreviewWithCard() {
  PreviewContainer {
    val unit = previewUnits.first()
    val expanded = remember { mutableStateOf(setOf(unit)) }
    UnitRow(
      unit = unit,
      settingsSource = MutableStateFlow(ForceSettings()),
      expandedUnitsState = expanded,
      showCards = true
    )
  }
}
