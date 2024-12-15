package tech.ajones.forcebuilder.model

class LibraryMini(
  val chassis: String,
  val variants: List<UnitVariant>,
  val id: Int
) {
  override fun hashCode(): Int = id
  override fun equals(other: Any?): Boolean =
    (other as? LibraryMini)?.id == id

  override fun toString(): String = "Mini[$id]: $chassis"
}
