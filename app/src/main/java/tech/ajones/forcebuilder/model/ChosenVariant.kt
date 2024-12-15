package tech.ajones.forcebuilder.model

data class ChosenVariant(
  val mini: Mini,
  val unit: UnitVariant
) {
  override fun toString(): String = unit.toString()
}

val Iterable<ChosenVariant>.pvSum: Int
  get() = sumOf { it.unit.pointsValue }
