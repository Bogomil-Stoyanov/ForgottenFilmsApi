package eu.bbsapps.forgottenfilmsapi.data.modules

import eu.bbsapps.forgottenfilmsapi.data.controllers.LoginController
import eu.bbsapps.forgottenfilmsapi.data.modules.util.GenericResponse
import eu.bbsapps.forgottenfilmsapi.data.requests.user.login.LoginAccountRequest
import eu.bbsapps.forgottenfilmsapi.data.responses.SimpleResponse
import io.ktor.http.*

object LoginModule {

    suspend fun login(request: LoginAccountRequest): GenericResponse<SimpleResponse> {
        val isPasswordCorrect = LoginController.isPasswordCorrect(request.email, request.password)

        return if (isPasswordCorrect) {
            GenericResponse(HttpStatusCode.OK, SimpleResponse(true, "Влязохте в системата"))
        } else {
            GenericResponse(HttpStatusCode.OK, SimpleResponse(false, "Грешен имейл или парола"))
        }
    }
}