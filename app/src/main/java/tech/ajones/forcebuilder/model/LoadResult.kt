package tech.ajones.forcebuilder.model

sealed class LoadResult<out T> {
  /**
   * The result is loading/processing. If non-null, [progress] should be a value in
   * [0, 1], where 0 is no progress, and 1 is finished.
   */
  open class Loading(val progress: Float? = null): LoadResult<Nothing>()

  /**
   * Loading result that allows cancelling the work in progress
   */
  class CancelableLoading(
    progress: Float? = null,
    val cancel: () -> Unit
  ): Loading(progress = progress)

  data class Success<T>(val data: T): LoadResult<T>()
  data class Failure(val message: String? = null): LoadResult<Nothing>()
}

fun <T, U> LoadResult<T>.map(transform: (T) -> U): LoadResult<U> =
  when (this) {
    is LoadResult.Success -> LoadResult.Success(transform(data))
    is LoadResult.Failure -> this
    is LoadResult.Loading -> this
  }
