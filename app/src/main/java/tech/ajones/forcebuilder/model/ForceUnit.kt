package tech.ajones.forcebuilder.model

data class ForceUnit(
  val mini: LibraryMini,
  val variant: UnitVariant
) {
  override fun toString(): String = variant.toString()
}

val Iterable<ForceUnit>.pvSum: Int
  get() = sumOf { it.variant.pointsValue }
