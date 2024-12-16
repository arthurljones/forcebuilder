package tech.ajones.forcebuilder.model

import org.junit.Assert.*
import org.junit.Test

class ForceUnitTest {
  @Test
  fun `pointsPerSkillIncrease produces the correct values`() {
    assertEquals(1, ForceUnit.pointsPerSkillIncrease(0))
    assertEquals(1, ForceUnit.pointsPerSkillIncrease(1))
    assertEquals(1, ForceUnit.pointsPerSkillIncrease(7))
    assertEquals(2, ForceUnit.pointsPerSkillIncrease(8))
    assertEquals(2, ForceUnit.pointsPerSkillIncrease(12))
    assertEquals(3, ForceUnit.pointsPerSkillIncrease(13))
    assertEquals(3, ForceUnit.pointsPerSkillIncrease(17))
    assertEquals(10, ForceUnit.pointsPerSkillIncrease(48))
    assertEquals(10, ForceUnit.pointsPerSkillIncrease(52))
    assertEquals(11, ForceUnit.pointsPerSkillIncrease(53))
  }

  @Test
  fun `pointsPerSkillDecrease produces the correct values`() {
    assertEquals(-1, ForceUnit.pointsPerSkillDecrease(0))
    assertEquals(-1, ForceUnit.pointsPerSkillDecrease(1))
    assertEquals(-1, ForceUnit.pointsPerSkillDecrease(14))
    assertEquals(-2, ForceUnit.pointsPerSkillDecrease(15))
    assertEquals(-2, ForceUnit.pointsPerSkillDecrease(24))
    assertEquals(-3, ForceUnit.pointsPerSkillDecrease(25))
    assertEquals(-3, ForceUnit.pointsPerSkillDecrease(34))
    assertEquals(-10, ForceUnit.pointsPerSkillDecrease(95))
    assertEquals(-10, ForceUnit.pointsPerSkillDecrease(104))
    assertEquals(-11, ForceUnit.pointsPerSkillDecrease(105))
  }

  @Test
  fun `modifiedPointsValue produces the correct values`() {
    assertEquals(0, ForceUnit.modifiedPointsValue(0, 4))
    assertEquals(20, ForceUnit.modifiedPointsValue(20, 4))
    assertEquals(50, ForceUnit.modifiedPointsValue(50, 4))

    assertEquals(18, ForceUnit.modifiedPointsValue(20, 5))
    assertEquals(16, ForceUnit.modifiedPointsValue(20, 6))
    assertEquals(60, ForceUnit.modifiedPointsValue(100, 8))

    assertEquals(24, ForceUnit.modifiedPointsValue(20, 3))
    assertEquals(28, ForceUnit.modifiedPointsValue(20, 2))
    assertEquals(80, ForceUnit.modifiedPointsValue(50, 1))
    assertEquals(180, ForceUnit.modifiedPointsValue(100, 0))
  }
}