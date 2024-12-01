package tech.ajones.forcebuilder.model

import tech.ajones.forcebuilder.MainActivityViewModel

enum class TechBase {
  IS, Clan
}

data class ForceSettings(
  val maxPointsValue: Int = 300,
  val library: MainActivityViewModel.MiniLibrary = MainActivityViewModel.MiniLibrary.Tomas,
  val techBase: Set<TechBase> = TechBase.entries.toSet()
)