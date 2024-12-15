package tech.ajones.forcebuilder.ui.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.ui.graphics.vector.ImageVector

data class UnitIcon(
  val icon: ImageVector,
  val contentDescription: String
)

val unitLockedIcon = UnitIcon(
  icon = Icons.Default.Lock,
  contentDescription = "Unit locked",
)

val unitSelectedIcon = UnitIcon(
  icon = Icons.Default.Check,
  contentDescription = "Unit selected"
)

val unitForbiddenIcon = UnitIcon(
  icon = Icons.Default.Error,
  contentDescription = "Unit forbidden"
)