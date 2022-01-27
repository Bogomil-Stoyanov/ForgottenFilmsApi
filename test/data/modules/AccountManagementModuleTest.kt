package data.modules

import data.validCreateAccountRequest
import eu.bbsapps.forgottenfilmsapi.data.modules.AccountManagementModule
import eu.bbsapps.forgottenfilmsapi.data.modules.RegisterModule
import eu.bbsapps.forgottenfilmsapi.database
import eu.bbsapps.forgottenfilmsapi.productionDatabase
import eu.bbsapps.forgottenfilmsapi.testDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AccountManagementModuleTest {

    @Before
    fun initialize() {
        database = testDatabase
        runBlocking {
            RegisterModule.register(validCreateAccountRequest)
        }
    }

    @After
    fun cleanUp() {
        database = productionDatabase
    }

    @Test
    fun changeNicknameSuccessful() = runBlocking {
        val response = AccountManagementModule.updateNickname("New Nick", validCreateAccountRequest.email)
        assertEquals("Успешно смени името си", response.data.message)
    }

    @Test
    fun addGenresToUser() = runBlocking {
        AccountManagementModule.addGenresToUser(listOf("genre"), validCreateAccountRequest.email)
        assertTrue(testDatabase.getUserGenres(validCreateAccountRequest.email).contains("genre"))
    }

    @Test
    fun updateUserGenres() = runBlocking {
        AccountManagementModule.updateUserGenres(listOf("genre", "genre1"), validCreateAccountRequest.email)
        assertTrue(testDatabase.getUserGenres(validCreateAccountRequest.email).contains("genre"))
        assertTrue(testDatabase.getUserGenres(validCreateAccountRequest.email).contains("genre1"))
    }

    @Test
    fun getUserGenres() = runBlocking {
        val userGenres = AccountManagementModule.getUserGenres(validCreateAccountRequest.email)
        assertTrue(testDatabase.getUserGenres(validCreateAccountRequest.email).containsAll(userGenres.data))
    }

    @Test
    fun addFilmToList() = runBlocking {
        val response = AccountManagementModule.addFilmToUserList("123", validCreateAccountRequest.email)
        assertEquals("Филмът е добавен към списъка ти", response.data.message)
        assertTrue(testDatabase.getUserFilmIdsList(validCreateAccountRequest.email).contains("123"))
    }

    @Test
    fun removeFilmInList() = runBlocking {
        AccountManagementModule.addFilmToUserList("123", validCreateAccountRequest.email)
        val response = AccountManagementModule.removeFilmFromUserList("123", validCreateAccountRequest.email)
        assertEquals("Филмът е премахнат от списъка ти", response.data.message)
        assertFalse(!testDatabase.getUserFilmIdsList(validCreateAccountRequest.email).contains("123"))
    }

    @Test
    fun removeFilmNotInList() = runBlocking {
        AccountManagementModule.removeFilmFromUserList("123", validCreateAccountRequest.email)
        AccountManagementModule.removeFilmFromUserList("123", validCreateAccountRequest.email)
        val response = AccountManagementModule.removeFilmFromUserList("123", validCreateAccountRequest.email)
        assertFalse(testDatabase.getUserFilmIdsList(validCreateAccountRequest.email).contains("123"))
    }


}