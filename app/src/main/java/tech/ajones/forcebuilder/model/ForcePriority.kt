package tech.ajones.forcebuilder.model

interface ForcePriority {
  fun scoreForce(force: Set<ForceUnit>): Double
}

class MaximizePointsValue: ForcePriority {
  override fun scoreForce(force: Set<ForceUnit>): Double = force.pointsValue.toDouble()
}
