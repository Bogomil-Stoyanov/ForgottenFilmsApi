package data.modules

import data.validCreateAccountRequest
import data.validLoginRequest
import eu.bbsapps.forgottenfilmsapi.data.modules.LoginModule
import eu.bbsapps.forgottenfilmsapi.data.modules.RegisterModule
import eu.bbsapps.forgottenfilmsapi.database
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
        assertEquals("Влязохте в системата", response.data.message)
    }

    @Test
    fun loginNotSuccessful() = runBlocking {
        RegisterModule.register(validCreateAccountRequest)
        val response = LoginModule.login(validLoginRequest.copy("badEmail"))
        assertEquals("Грешен имейл или парола", response.data.message)
    }
}