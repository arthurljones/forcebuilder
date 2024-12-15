package tech.ajones.forcebuilder.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
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

@Composable
fun UnitRow(
  unit: ChosenVariant,
  settingsSource: MutableStateFlow<ForceSettings>,
  selectedUnitState: MutableState<ChosenVariant?>,
  showCards: Boolean
) {
  Column(modifier = Modifier
    .clickable { selectedUnitState.value = unit.takeIf { selectedUnitState.value != unit } }
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
    unit.unit.also { variant ->
      variant.mulId?.takeIf { showCards }?.also {
        UnitCard(it)
      } ?: run {
        if (selectedUnitState.value != unit) {
          Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Text(variant.damageString)
            Text("[${variant.armorStructureString}]")
            Text("SZ ${variant.size}")
            Text("Intro ${variant.yearIntroduced}")
          }
        }
      }
    }
  }
}

@Preview
@Composable
private fun UnitRowPreviewNoCard() {
  PreviewContainer {
    val selected = remember { mutableStateOf<ChosenVariant?>(null) }
    UnitRow(
      unit = previewUnits.first(),
      settingsSource = MutableStateFlow(ForceSettings()),
      selectedUnitState = selected,
      showCards = false
    )
  }
}

@Preview
@Composable
private fun UnitRowPreviewWithCard() {
  PreviewContainer {
    val selected = remember { mutableStateOf<ChosenVariant?>(null) }
    UnitRow(
      unit = previewUnits.first(),
      settingsSource = MutableStateFlow(ForceSettings()),
      selectedUnitState = selected,
      showCards = true
    )
  }
}
