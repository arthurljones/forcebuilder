package tech.ajones.forcebuilder.ui.composable

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun IntField(
  value: Int?,
  onValueChange: (Int?) -> Unit,
  modifier: Modifier = Modifier,
  label: (@Composable () -> Unit)? = null,
) {
  TextField(
    value = value?.toString() ?: "",
    label = label,
    onValueChange = { onValueChange(it.toIntOrNull()) },
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    modifier = modifier,
  )
}