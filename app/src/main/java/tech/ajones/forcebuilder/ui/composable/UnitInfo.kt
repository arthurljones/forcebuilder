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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import tech.ajones.forcebuilder.model.ForceSettings
import tech.ajones.forcebuilder.model.ForceUnit
import tech.ajones.forcebuilder.model.LoadResult
import tech.ajones.forcebuilder.model.UnitVariant
import tech.ajones.forcebuilder.model.updateSuccess
import tech.ajones.forcebuilder.update

@Composable
fun UnitInfo(
  unit: ForceUnit,
  showMulCard: Boolean,
  settingsSource: MutableStateFlow<ForceSettings>,
  forceSource: MutableStateFlow<LoadResult<Set<ForceUnit>>?>,
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
      UnitLockButton(settingsSource, unit)
      ShowUnitVariantsButton(showVariantsState)
    }
    if (showVariantsState.value) {
      UnitVariantsList(settingsSource, forceSource, unit)
    }
  }
}

@Composable
private fun UnitLockButton(
  settingsSource: MutableStateFlow<ForceSettings>,
  unit: ForceUnit,
) {
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
    val lockedUnits = remember {
      settingsSource.map { it.lockedUnits }
    }.collectAsStateWithLifecycle(emptySet()).value
    val text = if (lockedUnits.contains(unit)) "Unlock" else "Lock"
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
  settingsSource: MutableStateFlow<ForceSettings>,
  forceSource: MutableStateFlow<LoadResult<Set<ForceUnit>>?>,
  unit: ForceUnit
) {
  val settings = settingsSource.collectAsStateWithLifecycle().value
  unit.mini.variants
    .sortedBy { it.variant }
    .forEach { possibleVariant ->
      Column(modifier = Modifier
        .padding(start = 24.dp)
        .fillMaxWidth()
        .clickable {
          if (unit.variant != possibleVariant) {
            val newUnit = unit.copy(variant = possibleVariant)
            forceSource.updateSuccess { it - unit + newUnit }
          }
        }
      ) {
        UnitRowHeader(
          unit = possibleVariant,
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
