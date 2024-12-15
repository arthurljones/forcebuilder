package tech.ajones.forcebuilder.model

class Mini(
  val chassis: String,
  val possibleUnits: List<UnitVariant>,
  val id: Int
) {
  override fun hashCode(): Int = id
  override fun equals(other: Any?): Boolean =
    (other as? Mini)?.id == id

  override fun toString(): String = "Mini[$id]: $chassis"
}
