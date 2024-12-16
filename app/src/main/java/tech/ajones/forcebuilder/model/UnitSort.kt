package tech.ajones.forcebuilder.model

sealed class UnitSortField<T: Comparable<T>>(
  val sortValue: (ForceUnit) -> T
) {
  data object ByName: UnitSortField<String>({ it.variant.chassis + it.variant.variant })
  data object ByTech: UnitSortField<Boolean>({ it.variant.isClan })
  data object ByPV: UnitSortField<Int>({ it.pointsValue })
  data object BySize: UnitSortField<Int>({ it.variant.size })
}

val UnitSortField<*>.name: String
  get() = when(this) {
    UnitSortField.ByName -> "Name"
    UnitSortField.ByPV -> "PV"
    UnitSortField.BySize -> "Size"
    UnitSortField.ByTech -> "Tech"
  }

data class UnitSortOrder<T: Comparable<T>, U: Comparable<U>>(
  val primary: UnitSortField<T>,
  val secondary: UnitSortField<U>?,
  val ascending: Boolean = true
) {
  val comparator: Comparator<ForceUnit>
    get() = Comparator { p0: ForceUnit, p1: ForceUnit ->
      val primary = primary.sortValue(p0).compareTo(primary.sortValue(p1))
      val result = if (primary == 0 && secondary != null) {
        secondary.sortValue(p0).compareTo(secondary.sortValue(p1))
      } else primary
      if (ascending) result else -result
    }

  companion object {
    // Secondary "constructor" for default U: Nothing generic type
    operator fun <T: Comparable<T>> invoke(
      primary: UnitSortField<T>,
      ascending: Boolean = true
    ) = UnitSortOrder<T, Nothing>(primary = primary, secondary = null, ascending = ascending)
  }
}