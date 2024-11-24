package tech.ajones.forcebuilder.model

data class UnitInfo(
  val chassis: String,
  val variant: String,
  val pointsValue: Int
) {
  override fun toString(): String = "$chassis $variant (PV: ${pointsValue})"
}