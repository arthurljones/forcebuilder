package tech.ajones.forcebuilder.model

import tech.ajones.forcebuilder.MainActivityViewModel

enum class TechBase {
  IS, Clan
}

data class ForceSettings(
  val library: MainActivityViewModel.MiniLibrary = MainActivityViewModel.MiniLibrary.Tomas,
  val techBase: Set<TechBase> = TechBase.entries.toSet(),
  val maxPointsValue: Int = 300,
  val maxUnits: Int? = null,
  val minUnits: Int? = null,
)