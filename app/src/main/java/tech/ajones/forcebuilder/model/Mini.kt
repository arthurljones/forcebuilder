package tech.ajones.forcebuilder.model

data class Mini(
  val chassis: String,
  val count: Int,
  val possibleUnits: List<UnitVariant>
)

data class ChosenVariant(
  val mini: Mini,
  val unit: UnitVariant
) {
  override fun toString(): String = unit.toString()
}