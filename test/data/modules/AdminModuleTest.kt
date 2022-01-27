package data.modules

import data.validAdminAccountRequest
import data.validCreateAccountRequest
import eu.bbsapps.forgottenfilmsapi.data.collections.Movie
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
    fun deleteMovieWithTitle() = runBlocking {

        AdminModule.deleteMovieWithName("Title")
        AdminModule.deleteMovieWithName("Title")
        AdminModule.deleteMovieWithName("Title")

        assertEquals(
            false,
            testDatabase.getAllMovies().contains(
                Movie(
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
    fun addMovies() = runBlocking {
        val response = AdminModule.insertMovies(
            listOf(
                Movie("Title", listOf("url"), "description", listOf("genre"), emptyList(), emptyList(), "url", "id1"),
                Movie(
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
            testDatabase.getAllMovies().contains(
                Movie(
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
    fun getMoviesCount() = runBlocking {
        AdminModule.insertMovies(
            listOf(
                Movie("Title", listOf("url"), "description", listOf("genre"), emptyList(), emptyList(), "url", "id1"),
                Movie(
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

        val response = AdminModule.getMovieCount()
        assertEquals(2, response.data)
    }

    @Test
    fun getUserCount() = runBlocking {
        val response = AdminModule.getMovieCount()
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