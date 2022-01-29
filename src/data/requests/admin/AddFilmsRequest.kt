package eu.bbsapps.forgottenfilmsapi.data.requests.admin

import eu.bbsapps.forgottenfilmsapi.data.collections.Film

data class AddFilmsRequest(
    val films: List<Film>
)