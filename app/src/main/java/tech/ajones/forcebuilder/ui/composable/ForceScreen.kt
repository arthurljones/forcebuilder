package tech.ajones.forcebuilder.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import tech.ajones.forcebuilder.model.ForceUnit
import tech.ajones.forcebuilder.model.ForceSettings
import tech.ajones.forcebuilder.model.LoadResult
import tech.ajones.forcebuilder.model.UnitSortField
import tech.ajones.forcebuilder.model.UnitSortOrder

@Composable
fun ForceScreen(
  forceSource: MutableStateFlow<LoadResult<Set<ForceUnit>>?>,
  settingSource: MutableStateFlow<ForceSettings>,
  sortSource: MutableStateFlow<UnitSortOrder<*, *>>,
  onRandomizeTap: () -> Unit
) {
  Column {
    GenerationOptions(
      settingsSource = settingSource,
      onRandomizeTap = onRandomizeTap
    )
    val forceResult = forceSource.collectAsStateWithLifecycle().value
    val sort = sortSource.collectAsStateWithLifecycle().value

    HorizontalDivider(Modifier.padding(vertical = 16.dp))
    Text("Force", style = MaterialTheme.typography.titleLarge)

    when (forceResult) {
      is LoadResult.Success -> {
        UnitList(
          units = forceResult.data.sortedWith(sort.comparator),
          sortSource = sortSource,
          settingsSource = settingSource,
          forceSource = forceSource,
        )

        HorizontalDivider(Modifier.padding(vertical = 16.dp))

        ListActions(forceResult.data)
      }
      is LoadResult.Loading -> {
        Text("Loading...")
        forceResult.progress?.also {
          LinearProgressIndicator(progress = { it })
        } ?: run {
          LinearProgressIndicator()
        }
        (forceResult as? LoadResult.CancelableLoading)?.also {
          Button(
            onClick = { forceResult.cancel() }
          ) {
            Text("Cancel")
          }
        }
      }
      is LoadResult.Failure -> {
        Text("Something went wrong: ${forceResult.message}")
      }
      null -> {
        Text("No force loaded")
      }
    }
  }
}

@Composable
private fun ForceScreenPreviewBase(
  force: LoadResult<Set<ForceUnit>>?,
) {
  PreviewContainer(modifier = Modifier.fillMaxSize()) {
    Box(modifier = Modifier
      .padding(16.dp)
      .verticalScroll(rememberScrollState())
    ) {
      ForceScreen(
        forceSource = MutableStateFlow(force),
        settingSource = MutableStateFlow(ForceSettings()),
        sortSource = MutableStateFlow(UnitSortOrder(primary = UnitSortField.ByName, ascending = true)),
        onRandomizeTap = { }
      )
    }
  }
}

@Preview(device = Devices.PIXEL_7)
@Composable
private fun ForceScreenSuccessPreview() {
  ForceScreenPreviewBase(force = LoadResult.Success(previewUnits.toSet()))
}

@Preview(device = Devices.PIXEL_7)
@Composable
private fun ForceScreenSuccessLockedPreview() {
  ForceScreenPreviewBase(
    force = LoadResult.Success(previewUnits.toSet()),
  )
}

@Preview(device = Devices.PIXEL_7)
@Composable
private fun ForceScreenLoadingPreview() {
  ForceScreenPreviewBase(force = LoadResult.CancelableLoading(
    progress = 0.35f,
    cancel = { }
  ))
}

@Preview(device = Devices.PIXEL_7)
@Composable
private fun ForceScreenEmptyPreview() {
  ForceScreenPreviewBase(force = null)
}

@Preview(device = Devices.PIXEL_7)
@Composable
private fun ForceScreenErrorPreview() {
  ForceScreenPreviewBase(force = LoadResult.Failure("failed to reticulate splines"))
}
