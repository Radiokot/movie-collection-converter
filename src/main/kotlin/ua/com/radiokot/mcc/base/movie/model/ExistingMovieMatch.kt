package ua.com.radiokot.mcc.base.movie.model

sealed class ExistingMovieMatch<M : ExistingMovie> {
    sealed class Found<M : ExistingMovie>(val movie: M) : ExistingMovieMatch<M>() {
        class Exact<M : ExistingMovie>(movie: M) : Found<M>(movie) {
            override fun toString(): String =
                "ExistingMovieMatch.Found.Exact(movie=$movie)"

        }

        class Maybe<M : ExistingMovie>(movie: M) : Found<M>(movie) {
            override fun toString(): String =
                "ExistingMovieMatch.Found.Maybe(movie=$movie)"

        }
    }

    class NotFound<M : ExistingMovie> : ExistingMovieMatch<M>()  {
        override fun toString(): String =
            "ExistingMovieMatch.NotFound"
    }
}