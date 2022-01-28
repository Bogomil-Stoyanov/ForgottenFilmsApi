package eu.bbsapps.forgottenfilmsapi.data.requests.admin

import eu.bbsapps.forgottenfilmsapi.data.collections.Movie

data class AddMoviesRequest(
    val films: List<Movie>
)