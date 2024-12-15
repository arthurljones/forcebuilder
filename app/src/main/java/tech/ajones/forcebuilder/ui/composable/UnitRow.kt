package tech.ajones.forcebuilder.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import tech.ajones.forcebuilder.model.ChosenVariant
import tech.ajones.forcebuilder.model.ForceSettings

@Composable
fun UnitRow(
  unit: ChosenVariant,
  settingsSource: MutableStateFlow<ForceSettings>,
  showCards: Boolean
) {
  Column {
    val settings by settingsSource.collectAsStateWithLifecycle()
    fun updateLocked(locked: (Set<ChosenVariant>) -> Boolean) {
      settingsSource.update { settings ->
        val newLocked = locked(settings.lockedUnits)
        val newLockedUnits = settings.lockedUnits
          .let { if (it.contains(unit)) it - unit else it + unit }
        settings.copy(lockedUnits = newLockedUnits)
      }
    }
    Row(modifier = Modifier
      .clickable { updateLocked { !it.contains(unit) } }
    ) {
      Checkbox(
        checked = settings.lockedUnits.contains(unit),
        modifier = Modifier.align(Alignment.CenterVertically),
        onCheckedChange = { checked -> updateLocked { checked } }
      )

      Column {
        Text(text = unit.toString())
        unit.unit.also { variant ->
          variant.mulId?.takeIf { showCards }?.also {
            UnitCard(it)
          } ?: run {
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
}

@Preview
@Composable
private fun UnitRowPreviewNoCard() {
  PreviewContainer {
    UnitRow(
      unit = previewUnits.first(),
      settingsSource = MutableStateFlow(ForceSettings()),
      showCards = false
    )
  }
}

@Preview
@Composable
private fun UnitRowPreviewWithCard() {
  PreviewContainer {
    UnitRow(
      unit = previewUnits.first(),
      settingsSource = MutableStateFlow(ForceSettings()),
      showCards = true
    )
  }
}
