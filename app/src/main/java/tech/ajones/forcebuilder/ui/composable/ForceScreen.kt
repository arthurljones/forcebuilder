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
import kotlinx.coroutines.flow.StateFlow
import tech.ajones.forcebuilder.CancelableLoading
import tech.ajones.forcebuilder.LoadResult
import tech.ajones.forcebuilder.model.ChosenVariant
import tech.ajones.forcebuilder.model.ForceSettings

@Composable
fun ForceScreen(
  forceSource: StateFlow<LoadResult<Set<ChosenVariant>>?>,
  settingSource: MutableStateFlow<ForceSettings>,
  lockedUnits: MutableStateFlow<Set<ChosenVariant>>,
  onRandomizeTap: () -> Unit
) {
  Column {
    GenerationOptions(
      settingsSource = settingSource,
      onRandomizeTap = onRandomizeTap
    )
    val result = forceSource.collectAsStateWithLifecycle().value

    HorizontalDivider(Modifier.padding(vertical = 16.dp))
    Text("Force", style = MaterialTheme.typography.titleLarge)

    when (result) {
      is LoadResult.Success -> {
        UnitList(
          units = result.data,
          lockedUnits = lockedUnits,
        )

        HorizontalDivider(Modifier.padding(vertical = 16.dp))

        ListActions(result.data)
      }
      is LoadResult.Loading -> {
        Text("Loading...")
        result.progress?.also {
          LinearProgressIndicator(progress = { it })
        } ?: run {
          LinearProgressIndicator()
        }
        (result as? CancelableLoading)?.also {
          Button(
            onClick = { result.cancel() }
          ) {
            Text("Cancel")
          }
        }
      }
      is LoadResult.Failure -> {
        Text("Something went wrong: ${result.message}")
      }
      null -> {
        Text("No force loaded")
      }
    }
  }
}

@Composable
private fun ForceScreenPreviewBase(
  force: LoadResult<Set<ChosenVariant>>?,
  lockedUnits: Set<ChosenVariant> = emptySet()
) {
  PreviewContainer(modifier = Modifier.fillMaxSize()) {
    Box(modifier = Modifier
      .padding(16.dp)
      .verticalScroll(rememberScrollState())
    ) {
      ForceScreen(
        forceSource = MutableStateFlow(force),
        lockedUnits = MutableStateFlow(lockedUnits),
        settingSource = MutableStateFlow(ForceSettings()),
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
    lockedUnits = setOf(previewUnits.first())
  )
}

@Preview(device = Devices.PIXEL_7)
@Composable
private fun ForceScreenLoadingPreview() {
  ForceScreenPreviewBase(force = CancelableLoading(
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
