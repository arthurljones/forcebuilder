package tech.ajones.forcebuilder.model

import java.util.concurrent.atomic.AtomicInteger

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

data class ChosenVariant(
  val mini: Mini,
  val unit: UnitVariant
) {
  override fun toString(): String = unit.toString()
}