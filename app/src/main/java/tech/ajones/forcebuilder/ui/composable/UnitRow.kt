package tech.ajones.forcebuilder.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.ajones.forcebuilder.model.ForceSettings
import tech.ajones.forcebuilder.model.ForceUnit
import tech.ajones.forcebuilder.model.LibraryMini
import tech.ajones.forcebuilder.toggle
import tech.ajones.forcebuilder.ui.binder.ForceSettingsUpdater
import tech.ajones.forcebuilder.ui.binder.ForceUpdater
import tech.ajones.forcebuilder.update

@Composable
fun UnitRow(
  unit: ForceUnit,
  settings: ForceSettings,
  settingsUpdater: ForceSettingsUpdater,
  forceUpdater: ForceUpdater,
  expandedUnitsState: MutableState<Set<LibraryMini>>,
  showCard: Boolean
) {
  Column(modifier = Modifier
    .fillMaxWidth()
    .clickable { expandedUnitsState.update { it.toggle(unit.mini) } }
  ) {
    UnitRowHeader(
      unit = unit,
      icons = listOfNotNull(
        unitLockedIcon.takeIf { settings.lockedUnits.contains(unit) }
      )
    )

    val variant = unit.variant
    if (expandedUnitsState.value.contains(unit.mini)) {
      UnitInfo(
        unit = unit,
        showMulCard = showCard,
        forceUpdater = forceUpdater,
        settings = settings,
        settingsUpdater = settingsUpdater,
      )
    } else {
      SimpleUnitInfo(variant)
    }
    HorizontalDivider()
  }
}

@Composable
fun UnitRowHeader(
  unit: ForceUnit,
  icons: List<UnitIcon> = emptyList()
) {
  Row(
    modifier = Modifier.fillMaxWidth()
  ){
    // Use up all remaining space after the icons render
    Box(modifier = Modifier.weight(1f)) {
      Text(
        text = unit.toString(),
        // Keep the text on the left/start if it's not as wide as the space
        modifier = Modifier.align(Alignment.CenterStart)
      )
    }
    icons.forEach {
      Icon(
        imageVector = it.icon,
        contentDescription = it.contentDescription,
        modifier = Modifier
          .widthIn(min = 18.dp)
          .align(Alignment.CenterVertically),
      )
    }
  }
}

@Preview
@Composable
private fun UnitRowPreview(
  unit: ForceUnit = previewUnits.first(),
  expandUnit: Boolean = false,
  showCard: Boolean = false
) {
  PreviewContainer {
    val expanded = remember { mutableStateOf(setOfNotNull(unit.mini.takeIf { expandUnit })) }
    UnitRow(
      unit = unit,
      settings = ForceSettings(),
      settingsUpdater = ForceSettingsUpdater.stub,
      forceUpdater = ForceUpdater.stub,
      expandedUnitsState = expanded,
      showCard = showCard
    )
  }
}

@Preview
@Composable
private fun UnitRowPreviewWithInfo() {
  UnitRowPreview(expandUnit = true)
}

@Preview
@Composable
private fun UnitRowPreviewWithCard() {
  UnitRowPreview(expandUnit = true, showCard = true)
}
