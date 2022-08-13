package ua.com.radiokot.mcc.base.movie.model

sealed class ExistingMovieMatch<M : ExistingMovie> {
    abstract val type: String

    sealed class Found<M : ExistingMovie>(val movie: M) : ExistingMovieMatch<M>() {
        class Exact<M : ExistingMovie>(movie: M) : Found<M>(movie) {
            override val type: String = TYPE

            override fun toString(): String =
                "ExistingMovieMatch.Found.Exact(movie=$movie)"

            companion object {
                const val TYPE = "Exact"
            }
        }

        class Maybe<M : ExistingMovie>(movie: M) : Found<M>(movie) {
            override val type: String = TYPE

            override fun toString(): String =
                "ExistingMovieMatch.Found.Maybe(movie=$movie)"

            companion object {
                const val TYPE = "Maybe"
            }
        }
    }

    class NotFound<M : ExistingMovie> : ExistingMovieMatch<M>() {
        override val type: String = TYPE

        override fun toString(): String =
            "ExistingMovieMatch.NotFound"

        companion object {
            const val TYPE = "Not found"
        }
    }
}