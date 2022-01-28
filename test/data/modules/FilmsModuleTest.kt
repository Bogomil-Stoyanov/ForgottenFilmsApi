package data.modules

import data.validCreateAccountRequest
import eu.bbsapps.forgottenfilmsapi.data.collections.Movie
import eu.bbsapps.forgottenfilmsapi.data.modules.AccountManagementModule
import eu.bbsapps.forgottenfilmsapi.data.modules.AdminModule
import eu.bbsapps.forgottenfilmsapi.data.modules.FilmsModule
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

class FilmsModuleTest {
    @Before
    fun initialize() {
        database = testDatabase
        runBlocking {
            val movie = Movie(
                name = "Film title",
                imageUrls = listOf("imageUrl1", "imageUrl2"),
                description = "Description",
                genres = listOf("Genre1", "Genre2"),
                likedBy = emptyList(),
                dislikedBy = emptyList(),
                url = "url.mp4"
            )
            AdminModule.insertMovies(listOf(movie))
            RegisterModule.register(validCreateAccountRequest)
        }
    }

    @After
    fun cleanUp() {
        database = productionDatabase
    }

    @Test
    fun filmLiked() = runBlocking {
        val filmId = database.getMovieIdWithName("Film title")
        var response = FilmsModule.filmLiked(validCreateAccountRequest.email, filmId)
        assertEquals(1, response.data)
        response = FilmsModule.filmLiked(validCreateAccountRequest.email, filmId)
        assertEquals(0, response.data)
    }

    @Test
    fun filmDisliked() = runBlocking {
        val filmId = database.getMovieIdWithName("Film title")
        var response = FilmsModule.filmDisliked(validCreateAccountRequest.email, filmId)
        assertEquals(-1, response.data)
        response = FilmsModule.filmDisliked(validCreateAccountRequest.email, filmId)
        assertEquals(0, response.data)
    }

    @Test
    fun likeCountForFilm() = runBlocking {
        val filmId = database.getMovieIdWithName("Film title")
        FilmsModule.filmLiked(validCreateAccountRequest.email, filmId)
        val likeCountForFilm = FilmsModule.getLikeCountForFilm(filmId).data
        assertEquals(1, likeCountForFilm)
        FilmsModule.filmDisliked(validCreateAccountRequest.email, filmId)
        val dislikeCountForFilm = FilmsModule.getDislikeCountForFilm(filmId).data
        assertEquals(1, dislikeCountForFilm)
    }

    @Test
    fun getFilmForUser() = runBlocking {
        val filmId = database.getMovieIdWithName("Film title")
        FilmsModule.filmLiked(validCreateAccountRequest.email, filmId)
        val film = FilmsModule.getFilmForUser(validCreateAccountRequest.email, filmId)
        assertEquals(1, film.data.isLiked)
        assertEquals(1, film.data.likes)
        assertEquals(0, film.data.dislikes)
    }

    @Test
    fun getFeed() = runBlocking {
        val feed = FilmsModule.getUserFeed(validCreateAccountRequest.email).data
        assertTrue(feed.size >= 2)
    }

    @Test
    fun searchFilm() = runBlocking {
        var films = FilmsModule.searchFilmByTitle("Film title").data
        assertTrue(films.isNotEmpty())
        films = FilmsModule.searchFilmByTitle("Non existent film").data
        assertTrue(films.isEmpty())
    }

    @Test
    fun filmInUserList() = runBlocking {
        val filmId = database.getMovieIdWithName("Film title")
        AccountManagementModule.addFilmToUserList(filmId, validCreateAccountRequest.email)
        var isFilmInUserList = FilmsModule.isFilmInUserList(validCreateAccountRequest.email, filmId).data
        assertTrue(isFilmInUserList)
        AccountManagementModule.removeFilmFromUserList(filmId, validCreateAccountRequest.email)
        isFilmInUserList = FilmsModule.isFilmInUserList(validCreateAccountRequest.email, filmId).data
        assertFalse(isFilmInUserList)
    }

    @Test
    fun getAllFilms() = runBlocking {
        val allFilms = FilmsModule.getAllFilms().data
        assertTrue(allFilms.isNotEmpty())
    }

    @Test
    fun getAllGenres() = runBlocking {
        val genres = FilmsModule.getAllGenres().data
        assertTrue(genres.isNotEmpty())
    }

}