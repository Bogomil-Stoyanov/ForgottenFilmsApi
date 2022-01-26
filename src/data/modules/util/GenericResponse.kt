package eu.bbsapps.forgottenfilmsapi.data.modules.util

import io.ktor.http.*

/**
 * A generic class used for communication between the Route and the Module
 */
data class GenericResponse<T>(
    val statusCode: HttpStatusCode,
    val data: T
)