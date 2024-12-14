package tech.ajones.forcebuilder.model

interface ForcePriority {
  fun scoreForce(force: Set<ChosenVariant>): Double
}

class MaximizePointsValue: ForcePriority {
  override fun scoreForce(force: Set<ChosenVariant>): Double = force.pvSum.toDouble()
}
