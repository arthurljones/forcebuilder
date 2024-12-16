package tech.ajones.forcebuilder.model

import kotlin.math.absoluteValue
import kotlin.math.ceil

data class ForceUnit(
  val mini: LibraryMini,
  val variant: UnitVariant,
  val skill: Int = 4
) {
  override fun toString(): String = "${variant.chassis} ${variant.variant} Skill $skill PV: $pointsValue"

  val pointsValue: Int
    get() = modifiedPointsValue(basePoints = variant.pointsValue, skill = skill)

  companion object {
    fun modifiedPointsValue(basePoints: Int, skill: Int): Int {
      val skillOffset = skill - 4
      val pointModPerSkillOffset = when {
        skillOffset < 0 -> pointsPerSkillIncrease(basePoints)
        else -> pointsPerSkillDecrease(basePoints)
      }
      return basePoints + skillOffset.absoluteValue * pointModPerSkillOffset
    }

    fun pointsPerSkillIncrease(basePoints: Int): Int =
      ceil((basePoints - 2) / 5.0).coerceAtLeast(1.0).toInt()

    fun pointsPerSkillDecrease(basePoints: Int): Int =
      -ceil((basePoints - 4) / 10.0).coerceAtLeast(1.0).toInt()
  }
}

val Iterable<ForceUnit>.pointsValue: Int
  get() = sumOf { it.pointsValue }
