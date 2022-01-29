package data.modules

import data.validAdminAccountRequest
import data.validCreateAccountRequest
import eu.bbsapps.forgottenfilmsapi.data.collections.Film
import eu.bbsapps.forgottenfilmsapi.data.modules.AdminModule
import eu.bbsapps.forgottenfilmsapi.data.modules.RegisterModule
import eu.bbsapps.forgottenfilmsapi.database
import eu.bbsapps.forgottenfilmsapi.productionDatabase
import eu.bbsapps.forgottenfilmsapi.routes.adminEmails
import eu.bbsapps.forgottenfilmsapi.testDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AdminModuleTest {

    @Before
    fun initialize() {
        database = testDatabase
        runBlocking {
            RegisterModule.register(validAdminAccountRequest)
        }
    }

    @After
    fun cleanUp() {
        database = productionDatabase
    }

    @Test()
    fun deleteFilmWithTitle() = runBlocking {

        AdminModule.deleteFilmWithName("Title")
        AdminModule.deleteFilmWithName("Title")
        AdminModule.deleteFilmWithName("Title")

        assertEquals(
            false,
            testDatabase.getAllFilms().contains(
                Film(
                    "Title",
                    listOf("url"),
                    "description",
                    listOf("genre"),
                    emptyList(),
                    emptyList(),
                    "url",
                    "id1"
                )
            )
        )
    }

    @Test
    fun addFilms() = runBlocking {
        val response = AdminModule.insertFilms(
            listOf(
                Film("Title", listOf("url"), "description", listOf("genre"), emptyList(), emptyList(), "url", "id1"),
                Film(
                    "Title1",
                    listOf("url1"),
                    "description1",
                    listOf("genre1"),
                    emptyList(),
                    emptyList(),
                    "url1",
                    "id2"
                ),
            )
        )
        assertTrue(response.data.contains("id1"))
        assertTrue(response.data.contains("id2"))
        assertTrue(
            testDatabase.getAllFilms().contains(
                Film(
                    "Title",
                    listOf("url"),
                    "description",
                    listOf("genre"),
                    emptyList(),
                    emptyList(),
                    "url",
                    "id1"
                )
            )
        )
    }

    @Test
    fun getFilmsCount() = runBlocking {
        AdminModule.insertFilms(
            listOf(
                Film("Title", listOf("url"), "description", listOf("genre"), emptyList(), emptyList(), "url", "id1"),
                Film(
                    "Title1",
                    listOf("url1"),
                    "description1",
                    listOf("genre1"),
                    emptyList(),
                    emptyList(),
                    "url1",
                    "id2"
                ),
            )
        )

        val response = AdminModule.getFilmCount()
        assertTrue(response.data > 0)
    }

    @Test
    fun getUserCount() = runBlocking {
        val response = AdminModule.getFilmCount()
        assertTrue(response.data >= 0)
    }

    @Test
    fun isAdminTrue() {
        val response = AdminModule.isAdmin(adminEmails[0])
        assertTrue(response.data)
    }

    @Test
    fun isAdminFalse() {
        val response = AdminModule.isAdmin(validCreateAccountRequest.email)
        assertFalse(response.data)
    }
}