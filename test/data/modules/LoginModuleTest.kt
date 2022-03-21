package data.modules

import data.validCreateAccountRequest
import data.validLoginRequest
import eu.bbsapps.forgottenfilmsapi.data.modules.LoginModule
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

class LoginModuleTest {

    @Before
    fun initialize() {
        database = testDatabase
    }

    @After
    fun cleanUp() {
        database = productionDatabase
    }

    @Test
    fun loginSuccessful() = runBlocking {
        RegisterModule.register(validCreateAccountRequest)
        val response = LoginModule.login(validLoginRequest)
        assertEquals(localizedResponses.getLocalisedValue("logged_in"), response.data.message)
    }

    @Test
    fun loginNotSuccessful() = runBlocking {
        RegisterModule.register(validCreateAccountRequest)
        val response = LoginModule.login(validLoginRequest.copy("badEmail"))
        assertEquals(localizedResponses.getLocalisedValue("wrong_credentials"), response.data.message)
    }
}