package data.modules

import data.validCreateAccountRequest
import eu.bbsapps.forgottenfilmsapi.data.modules.RegisterModule
import eu.bbsapps.forgottenfilmsapi.database
import eu.bbsapps.forgottenfilmsapi.localizedResponses
import eu.bbsapps.forgottenfilmsapi.productionDatabase
import eu.bbsapps.forgottenfilmsapi.testDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class RegisterModuleTest {

    @Before
    fun initialize() {
        database = testDatabase
    }

    @After
    fun cleanUp() {
        database = productionDatabase
    }

    @Test
    fun registerNotSuccessful_invalidEmail() = runBlocking {
        val response = RegisterModule.register(
            validCreateAccountRequest.copy(email = "invalidEmail")
        )
        assertEquals(localizedResponses.getLocalisedValue("invalid_email"), response.data.message)
    }

    @Test
    fun registerNotSuccessful_invalidPassword1() = runBlocking {
        val response = RegisterModule.register(
            validCreateAccountRequest.copy(password = "badpass")
        )
        assertEquals(
            localizedResponses.getLocalisedValue("password_requirements"),
            response.data.message
        )
    }

    @Test
    fun registerNotSuccessful_invalidPassword2() = runBlocking {
        val response = RegisterModule.register(
            validCreateAccountRequest.copy(password = "Pass_")
        )
        assertEquals(
            localizedResponses.getLocalisedValue("password_requirements"),
            response.data.message
        )
    }

    @Test
    fun registerNotSuccessful_invalidNickname() = runBlocking {
        val response = RegisterModule.register(
            validCreateAccountRequest.copy(nickname = "  ")
        )
        assertEquals( localizedResponses.getLocalisedValue("name_cannot_be_empty"), response.data.message)
    }

    @Test
    fun registerSuccessful() = runBlocking {
        val response = RegisterModule.register(
            validCreateAccountRequest.copy()
        )
        assertEquals(localizedResponses.getLocalisedValue("account_created_successfully"), response.data.message)
    }

}