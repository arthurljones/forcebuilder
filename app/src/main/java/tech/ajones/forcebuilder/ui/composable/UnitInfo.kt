package tech.ajones.forcebuilder.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tech.ajones.forcebuilder.model.ForceSettings
import tech.ajones.forcebuilder.model.ForceUnit
import tech.ajones.forcebuilder.model.UnitVariant
import tech.ajones.forcebuilder.ui.binder.ForceSettingsUpdater
import tech.ajones.forcebuilder.ui.binder.ForceUpdater
import tech.ajones.forcebuilder.update

@Composable
fun UnitInfo(
  unit: ForceUnit,
  showMulCard: Boolean,
  forceUpdater: ForceUpdater,
  settings: ForceSettings,
  settingsUpdater: ForceSettingsUpdater,
  modifier: Modifier = Modifier
) {
  val showVariantsState = remember { mutableStateOf(false) }
  Column(modifier = modifier) {
    val variant = unit.variant
    variant.mulId?.takeIf { showMulCard }?.also {
      UnitCard(it)
    } ?: run {
      Column(modifier = Modifier.padding(start = 32.dp)) {
        Text("Mini: ${unit.mini.chassis}")
        Text("Base PV: ${unit.variant.pointsValue}")
        Text("TP: ${variant.type} SZ: ${variant.size} TMM: ${variant.tmm} MV: ${variant.movement}")
        Text("Role: ${variant.role}")
        // TODO: Should be slider or other explicit list
        IntField(
          value = unit.skill,
          onValueChange = { newSkill ->
            forceUpdater.replaceUnit(
              unit = unit,
              replacement = unit.copy(skill = newSkill ?: settings.defaultSkill)
            )
          },
          label = {
            Text("Skill")
          }
        )
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
      UnitLockButton(
        unit = unit,
        settings = settings,
        settingsUpdater = settingsUpdater
      )
      ShowUnitVariantsButton(showVariantsState)
    }
    if (showVariantsState.value) {
      UnitVariantsList(
        unit = unit,
        settings = settings,
        forceUpdater = forceUpdater
      )
    }
  }
}

@Composable
private fun UnitLockButton(
  unit: ForceUnit,
  settings: ForceSettings,
  settingsUpdater: ForceSettingsUpdater
) {
  Button(
    onClick = { settingsUpdater.toggleUnitLocked(unit) }
  ) {
    val text = if (settings.lockedUnits.contains(unit)) "Unlock" else "Lock"
    Text(text)
  }
}

@Composable
private fun ShowUnitVariantsButton(
  showVariants: MutableState<Boolean>
) {
  Button(
    onClick = { showVariants.update { !it } }
  ) {
    val text = if (showVariants.value) "Hide Variants" else "Show Variants"
    Text(text)
  }
}

@Composable
private fun UnitVariantsList(
  unit: ForceUnit,
  settings: ForceSettings,
  forceUpdater: ForceUpdater,
) {
  unit.mini.variants
    .sortedBy { it.variant }
    .forEach { possibleVariant ->
      val possibleUnit = unit.copy(variant = possibleVariant)
      Column(modifier = Modifier
        .padding(start = 24.dp)
        .fillMaxWidth()
        .clickable { forceUpdater.replaceUnit(unit, possibleUnit) }
      ) {
        UnitRowHeader(
          unit = possibleUnit,
          icons = listOfNotNull(
            unitSelectedIcon.takeIf { unit.variant == possibleVariant },
            unitForbiddenIcon.takeIf { !settings.scorer.unitCouldMeet(possibleVariant) }
          )
        )
        SimpleUnitInfo(possibleVariant)
        HorizontalDivider()
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
