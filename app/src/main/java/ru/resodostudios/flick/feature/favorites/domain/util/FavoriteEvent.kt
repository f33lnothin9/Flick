package ru.resodostudios.flick.feature.favorites.domain.util

import ru.resodostudios.flick.feature.favorites.domain.model.FavoriteMovie
import ru.resodostudios.flick.feature.movie.data.model.Movie

sealed interface FavoriteEvent {

    data class AddMovie(val movie: Movie) : FavoriteEvent
    data class DeleteMovie(val movie: FavoriteMovie) : FavoriteEvent
}