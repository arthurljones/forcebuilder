package tech.ajones.forcebuilder.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.compose.AppTheme
import tech.ajones.forcebuilder.model.ChosenVariant
import tech.ajones.forcebuilder.model.Damage
import tech.ajones.forcebuilder.model.Mini
import tech.ajones.forcebuilder.model.UnitVariant

@Composable
fun PreviewContainer(
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  AppTheme {
    Box(
      modifier = Modifier
        .background(MaterialTheme.colorScheme.surface)
        .then(modifier)
    ) {
      content()
    }
  }
}

val previewUnits: List<ChosenVariant> = listOf(
  Mini(
    chassis = "Atlas",
    count = 1,
    possibleUnits = listOf(
      UnitVariant(
        chassis = "Atlas",
        variant = "AS7-D",
        isClan = false,
        pointsValue = 52,
        type = "BM",
        size = 4,
        tmm = 1,
        movement = "6",
        role = "Juggernaut",
        damage = Damage(short = "5", medium = "5", long = "2"),
        overheat = 0,
        armor = 10,
        structure = 8,
        specials = "AC2/2/-, IF1, LRM1/1/1, REAR1/1/-",
        yearIntroduced = 2775,
      ),
      UnitVariant(
        chassis = "Atlas",
        variant = "AS7-D",
        isClan = false,
        pointsValue = 55,
        type = "BM",
        size = 4,
        tmm = 1,
        movement = "6",
        role = "Juggernaut",
        damage = Damage(short = "3", medium = "4", long = "3"),
        overheat = 1,
        armor = 10,
        structure = 8,
        specials = "C3S, ECM, IF1, LRM1/1/1, MHQ1, REAR1/1/-",
        yearIntroduced = 3070,
      )
    )
  ),
  Mini(
    chassis = "Timber Wolf",
    count = 1,
    possibleUnits = listOf(
      UnitVariant(
        chassis = "Mad Cat",
        clanChassis = "Timber Wolf",
        isClan = true,
        variant = "Prime",
        pointsValue = 54,
        type = "BM",
        size = 3,
        tmm = 2,
        movement = "10",
        role = "Brawler",
        damage = Damage(short = "5", medium = "5", long = "4"),
        overheat = 1,
        armor = 8,
        structure = 4,
        specials = "CASE, IF2, LRM1/1/2, OMNI",
        yearIntroduced = 2945,
      ),
      UnitVariant(
        chassis = "Mad Cat",
        clanChassis = "Timber Wolf",
        isClan = true,
        variant = "T",
        pointsValue = 62,
        type = "BM",
        size = 3,
        tmm = 2,
        movement = "10",
        role = "Missile Boat",
        damage = Damage(short = "7", medium = "7", long = "4"),
        overheat = 2,
        armor = 8,
        structure = 4,
        specials = "CASE, IF2, OMNI",
        yearIntroduced = 3142,
      )
    )
  ),
  Mini(
    chassis = "Marauder",
    count = 1,
    possibleUnits = listOf(
      UnitVariant(
        chassis = "Marauder",
        isClan = false,
        variant = "MAD-3D",
        pointsValue = 35,
        type = "BM",
        size = 3,
        tmm = 1,
        movement = "8",
        role = "Brawler",
        damage = Damage(short = "3", medium = "3", long = "2"),
        overheat = 1,
        armor = 7,
        structure = 7,
        specials = "ENE",
        yearIntroduced = 2834,
      ),
      UnitVariant(
        chassis = "Marauder",
        isClan = false,
        variant = "MAD-5L",
        pointsValue = 42,
        type = "BM",
        size = 3,
        tmm = 1,
        movement = "8",
        role = "Sniper",
        damage = Damage(short = "3", medium = "3", long = "2"),
        overheat = 1,
        armor = 7,
        structure = 6,
        specials = "ECM, ENE, OVL, STL, TSM",
        yearIntroduced = 3067,
      ),
    )
  )
).map { ChosenVariant(mini = it, unit = it.possibleUnits.first() ) }