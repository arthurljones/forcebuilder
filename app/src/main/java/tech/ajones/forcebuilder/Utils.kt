package tech.ajones.forcebuilder

import androidx.compose.runtime.MutableState

operator fun <T> List<T>.component6(): T = get(5)
operator fun <T> List<T>.component7(): T = get(6)

/**
 * Toggles [item] in receiver - it will be removed if it is already present, or added
 * if it is not present.
 */
fun <T> Set<T>.toggle(item: T): Set<T> =
  if (contains(item)) this - item else this + item

/**
 * Passes the current [MutableState.value] to [transform], then assigns
 * the result to [MutableState.value]
 */
fun <T> MutableState<T>.update(transform: (T) -> T) {
  value = transform(value)
}