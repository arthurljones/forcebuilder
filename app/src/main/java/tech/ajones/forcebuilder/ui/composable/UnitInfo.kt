package tech.ajones.forcebuilder.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import tech.ajones.forcebuilder.model.ChosenVariant
import tech.ajones.forcebuilder.model.ForceSettings
import tech.ajones.forcebuilder.model.UnitVariant

@Composable
fun UnitInfo(
  unit: ChosenVariant,
  showMulCard: Boolean,
  settingsSource: MutableStateFlow<ForceSettings>,
  modifier: Modifier = Modifier
) {
  val settings = settingsSource.collectAsStateWithLifecycle().value
  Column(modifier = modifier) {
    val variant = unit.unit
    variant.mulId?.takeIf { showMulCard }?.also {
      UnitCard(it)
    } ?: run {
      Column(modifier = Modifier.padding(start = 32.dp)) {
        Text("TP: ${variant.type} SZ: ${variant.size} TMM: ${variant.tmm} MV: ${variant.movement}")
        Text("Role: ${variant.role} Skill: 4") // TODO: Adjustable skill
        Text("OV: ${variant.overheat} DMG: ${variant.damageString}")
        Text("A: ${variant.armor}, S: ${variant.structure}")
        Text("Special: ${variant.specials}")
        val firstYear = variant.yearIntroduced.takeIf { it > 0 } ?: "N/A"
        val advYear = variant.advancedTechYear.takeIf { it > 0 } ?: "N/A"
        val stdYear = variant.standardTechYear.takeIf { it > 0 } ?: "N/A"
        Text("Available: First: $firstYear, Adv: $advYear, Std: $stdYear")
      }
    }
    Row {
      Button(
        onClick = {
          settingsSource.update { settings ->
            val lockedUnits = settings.lockedUnits
            val unitLocked = lockedUnits.contains(unit)
            val newLockedUnits = if (unitLocked) {
              lockedUnits - unit
            } else {
              lockedUnits + unit
            }
            settings.copy(lockedUnits = newLockedUnits)
          }
        }
      ) {
        val text = if (settings.lockedUnits.contains(unit)) "Unlock" else "Lock"
        Text(text)
      }
    }
  }
}

@Composable
fun SimpleUnitInfo(unit: UnitVariant) {
  Row(
    horizontalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    Text(unit.damageString)
    Text("[${unit.armorStructureString}]")
    Text("SZ ${unit.size}")
    Text("Intro ${unit.yearIntroduced}")
  }
}
