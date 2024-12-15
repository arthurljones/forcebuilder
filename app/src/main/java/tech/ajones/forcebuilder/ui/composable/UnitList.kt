package tech.ajones.forcebuilder.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.UnfoldLess
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import tech.ajones.forcebuilder.model.ForceUnit
import tech.ajones.forcebuilder.model.ForceSettings
import tech.ajones.forcebuilder.model.LibraryMini
import tech.ajones.forcebuilder.model.LoadResult
import tech.ajones.forcebuilder.model.UnitSortField
import tech.ajones.forcebuilder.model.UnitSortOrder
import tech.ajones.forcebuilder.model.name

private val sortByOptions = listOf(
  UnitSortField.ByName,
  UnitSortField.ByPV,
  UnitSortField.BySize,
  UnitSortField.ByTech
)

private val UnitSortOrder<*,*>.directionIcon: ImageVector
  get() = if (ascending) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

@Composable
fun UnitList(
  units: Collection<ForceUnit>,
  forceSource: MutableStateFlow<LoadResult<Set<ForceUnit>>?>,
  settingsSource: MutableStateFlow<ForceSettings>,
  sortSource: MutableStateFlow<UnitSortOrder<*, *>>
) {
  // The expanded unit list is stored as minis instead of as force units so that
  // minis stay expanded when their variant is changed
  val expandedUnits = remember { mutableStateOf<Set<LibraryMini>>(emptySet()) }

  Column {
    var showCards by remember { mutableStateOf(false) }
    val statsStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)
    Text("Count: ${units.size}", style = statsStyle)
    Text("PV: ${units.sumOf { it.variant.pointsValue }}", style = statsStyle)

    Box {
      var sortExpanded by remember { mutableStateOf(false) }
      val sort = sortSource.collectAsStateWithLifecycle().value

      Button(
        onClick = { sortExpanded = true }
      ) {
        Text(text = "Sort By:")
        Spacer(modifier = Modifier.size(4.dp))
        Icon(imageVector = sort.directionIcon, contentDescription = "Sort direction")
        Text(text = sort.primary.name)
      }

      DropdownMenu(
        expanded = sortExpanded,
        onDismissRequest = { sortExpanded = false }
      ) {
        sortByOptions.forEach { sortBy ->
          val selected = sort.primary == sortBy
          val option = if (selected) {
            sort.copy(ascending = !sort.ascending)
          } else {
            UnitSortOrder(sortBy)
          }
          DropdownMenuItem(
            onClick = {
              sortSource.value = option
              sortExpanded = false
            },
            leadingIcon = { Icon(imageVector = option.directionIcon, contentDescription = "Sort direction") },
            text =  { Text(text = option.primary.name) }
          )
        }
      }
    }

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
      Spacer(modifier = Modifier.weight(1f))
      IconButton(
        onClick = { expandedUnits.value = units.map { it.mini }.toSet() }
      ) {
        Icon(Icons.Default.UnfoldMore, "Expand all units")
      }
      IconButton(
        onClick = { expandedUnits.value = emptySet() }
      ) {
        Icon(Icons.Default.UnfoldLess, "Collapse all units")
      }
    }
    HorizontalDivider(modifier = Modifier.height(8.dp))
    units.forEach { unit ->
      UnitRow(
        unit = unit,
        settingsSource = settingsSource,
        forceSource = forceSource,
        expandedUnitsState = expandedUnits,
        showCard = showCards
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
      sortSource = MutableStateFlow(UnitSortOrder(primary = UnitSortField.ByName, ascending = true)),
      settingsSource = MutableStateFlow(ForceSettings()),
      forceSource = MutableStateFlow(LoadResult.Success(previewUnits.toSet()))
    )
  }
}
