package tech.ajones.forcebuilder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.compose.AppTheme
import tech.ajones.forcebuilder.ui.composable.ForceScreen

class MainActivity: ComponentActivity() {
  private val model: MainActivityViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    model.setup(
      context = this,
      allUnitsPath = "all-meks.json"
    )

    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val snackbarHostState = remember { SnackbarHostState() }
      AppTheme {
        Scaffold(
          snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->
          Box(
            modifier = Modifier
              .padding(padding)
              .padding(horizontal = 16.dp)
              .verticalScroll(rememberScrollState())
          ) {
            ForceScreen(
              units = model.chosen.collectAsStateWithLifecycle().value?.toList(),
              settingSource = model.forceSettings,
              lockedUnits = model.lockedUnits,
              onRandomizeTap = model::onRandomizeTap,
            )
          }
        }
      }
    }
  }
}

