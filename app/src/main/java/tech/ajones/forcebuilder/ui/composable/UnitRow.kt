package tech.ajones.forcebuilder.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import tech.ajones.forcebuilder.model.ForceUnit
import tech.ajones.forcebuilder.model.ForceSettings
import tech.ajones.forcebuilder.model.LibraryMini
import tech.ajones.forcebuilder.model.LoadResult
import tech.ajones.forcebuilder.model.UnitVariant
import tech.ajones.forcebuilder.toggle
import tech.ajones.forcebuilder.update

@Composable
fun UnitRow(
  unit: ForceUnit,
  settingsSource: MutableStateFlow<ForceSettings>,
  forceSource: MutableStateFlow<LoadResult<Set<ForceUnit>>?>,
  expandedUnitsState: MutableState<Set<LibraryMini>>,
  showCard: Boolean
) {
  Column(modifier = Modifier
    .fillMaxWidth()
    .clickable { expandedUnitsState.update { it.toggle(unit.mini) } }
  ) {
    val lockedUnits = remember {
      settingsSource.map { it.lockedUnits }
    }.collectAsStateWithLifecycle(emptySet()).value
    UnitRowHeader(
      unit = unit.variant,
      icons = listOfNotNull(
        unitLockedIcon.takeIf { lockedUnits.contains(unit) }
      )
    )

    val variant = unit.variant
    if (expandedUnitsState.value.contains(unit.mini)) {
      UnitInfo(
        unit = unit,
        showMulCard = showCard,
        settingsSource = settingsSource,
        forceSource = forceSource,
      )
    } else {
      SimpleUnitInfo(variant)
    }
    HorizontalDivider()
  }
}

@Composable
fun UnitRowHeader(
  unit: UnitVariant,
  icons: List<UnitIcon> = emptyList()
) {
  Row {
    Text(text = unit.toString())
    Spacer(modifier = Modifier.weight(1f))
    icons.forEach {
      Icon(
        imageVector = it.icon,
        contentDescription = it.contentDescription,
        modifier = Modifier
          .size(18.dp)
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
      settingsSource = MutableStateFlow(ForceSettings()),
      expandedUnitsState = expanded,
      forceSource = MutableStateFlow(LoadResult.Success(setOf(unit))),
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
