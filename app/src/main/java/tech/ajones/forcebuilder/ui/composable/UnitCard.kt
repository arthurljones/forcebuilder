package tech.ajones.forcebuilder.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import tech.ajones.forcebuilder.R

@Composable
fun UnitCard(
  mulId: Int,
  skill: Int = 4,
  modifier: Modifier = Modifier
) {
  val painter = rememberAsyncImagePainter(
    model = if (LocalInspectionMode.current) {
      R.drawable.alphastrike_card_blank
    } else {
      "http://masterunitlist.info/Unit/Card/$mulId?skill=$skill"
    }
  )
  Image(
    painter = painter,
    contentDescription = "Alpha Strike Card",
    modifier = modifier
  )
  val state = painter.state.collectAsStateWithLifecycle().value
  when (state) {
    is AsyncImagePainter.State.Error -> {
      Text(
        text = "error loading ${mulId}: ${state.result.throwable.message}",
        modifier = modifier
      )
    }
    else -> { }
  }
}

@Preview
@Composable
private fun ASCardPreview() {
  PreviewContainer {
    // Atlas AS7-D
    UnitCard(140)
  }
}