package tech.ajones.forcebuilder.ui.composable

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.ajones.forcebuilder.model.ChosenVariant

@Composable
fun ListActions(
  units: List<ChosenVariant>
) {
  Column {
    Text("Actions", style = MaterialTheme.typography.titleLarge)

    Row(
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      val text = units.joinToString("\n")
      val clipboard = LocalClipboardManager.current
      Button(
        enabled = units.isNotEmpty(),
        onClick = { clipboard.setText(AnnotatedString(text)) }
      ) {
        Text("Copy To Clipboard")
      }

      val context = LocalContext.current
      Button(
        onClick = {
          val sendIntent: Intent = Intent().also {
            it.action = Intent.ACTION_SEND
            it.putExtra(Intent.EXTRA_TEXT, text)
            it.type = "text/plain"
          }
          val shareIntent = Intent.createChooser(sendIntent, null)
          context.startActivity(shareIntent)
        }
      ) {
        Text("Share")
      }
    }
  }
}

@Preview
@Composable
private fun ListActionsPreview() {
  PreviewContainer {
    ListActions(
      units = previewUnits
    )
  }
}