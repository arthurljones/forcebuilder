package tech.ajones.forcebuilder.model

import tech.ajones.forcebuilder.MainActivityViewModel

data class ForceSettings(
  val maxPointsValue: Int = 300,
  val library: MainActivityViewModel.MiniLibrary = MainActivityViewModel.MiniLibrary.Tomas,
)