package eu.bbsapps.forgottenfilmsapi.data.responses

import eu.bbsapps.forgottenfilmsapi.data.collections.FilmFeedItem

data class FilmFeedResponse(
    val title: String,
    val films: List<FilmFeedItem>
)
