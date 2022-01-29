package eu.bbsapps.forgottenfilmsapi.data

import eu.bbsapps.forgottenfilmsapi.data.collections.Film
import eu.bbsapps.forgottenfilmsapi.data.collections.User
import eu.bbsapps.forgottenfilmsapi.data.responses.GenreWatchTimePair
import eu.bbsapps.forgottenfilmsapi.security.checkHashForPassword


/**
 * Database for local tests which do not affect the production database
 */
class TestDatabase : DataAccessObject {

    private val users = mutableListOf<User>()
    private val films = mutableListOf<Film>()

    override suspend fun registerUser(user: User): Boolean {
        return users.add(user)
    }

    override suspend fun checkIfUserExists(email: String): Boolean {
        return users.any { user -> user.email == email }
    }

    override suspend fun checkPasswordForEmail(email: String, passwordToCheck: String): Boolean {
        val actualPassword = users.findLast { user ->
            user.email == email
        }?.password ?: return false
        return checkHashForPassword(passwordToCheck, actualPassword)
    }

    override suspend fun changeUserNickname(email: String, newNickname: String): Boolean {
        val user = users.findLast { user ->
            user.email == email
        } ?: return false

        users.remove(user)
        return users.add(user.copy(nickname = newNickname))
    }

    override suspend fun addUserGenre(email: String, genre: String): Boolean {
        val user = users.findLast { user ->
            user.email == email
        } ?: return false

        users.remove(user)
        return users.add(user.copy(genres = user.genres + genre))
    }

    override suspend fun deleteAllUserGenres(email: String) {
        val user = users.findLast { user ->
            user.email == email
        }

        user?.let {
            users.remove(user)
            users.add(user.copy(genres = emptyList()))
        }
    }

    override suspend fun getUserGenres(email: String): List<String> {
        return users.findLast { user ->
            user.email == email
        }?.genres ?: emptyList()
    }

    override suspend fun addFilmToUserList(email: String, filmId: String): Boolean {
        val user = users.findLast { user ->
            user.email == email
        } ?: return false

        users.remove(user)
        return users.add(user.copy(filmList = user.filmList + filmId))

    }

    override suspend fun removeFilmFromUserList(email: String, filmId: String): Boolean {
        val user = users.findLast { user ->
            user.email == email
        } ?: return false

        users.remove(user)
        return users.add(user.copy(filmList = user.filmList - filmId))
    }

    override suspend fun getUserFilmIdsList(email: String): List<String> {
        return users.findLast { user ->
            user.email == email
        }?.filmList ?: emptyList()
    }

    override suspend fun addFilm(film: Film): Boolean {
        films.add(film)
        return true
    }

    override suspend fun deleteFilmWithId(id: String): Boolean {
        return films.removeAll { film -> film.id == id }
    }

    override suspend fun getFilmWithId(id: String): Film {
        return films.findLast { film -> film.id == id } ?: Film(
            "null2",
            emptyList(),
            "null",
            emptyList(),
            emptyList(),
            emptyList(),
            "null",
            "null"
        )
    }

    override suspend fun isFilmLikedBy(filmId: String, userId: String): Boolean {
        return films.findLast { film ->
            film.id == filmId
        }?.likedBy?.contains(userId) ?: false
    }

    override suspend fun isFilmDislikedBy(filmId: String, userId: String): Boolean {
        return films.findLast { film ->
            film.id == filmId
        }?.dislikedBy?.contains(userId) ?: false
    }

    override suspend fun getUserIdByEmail(email: String): String {
        return users.findLast { user -> user.email == email }?.id ?: ""
    }

    override suspend fun addLikeToFilm(filmId: String, userId: String): Boolean {
        val film = films.findLast { film ->
            film.id == filmId
        } ?: return false

        films.remove(film)
        return films.add(film.copy(likedBy = film.likedBy + userId))
    }

    override suspend fun removeLikeFromFilm(filmId: String, userId: String): Boolean {
        val film = films.findLast { film ->
            film.id == filmId
        } ?: return false

        films.remove(film)
        return films.add(film.copy(likedBy = film.likedBy - userId))
    }

    override suspend fun addDislikeToFilm(filmId: String, userId: String): Boolean {
        val film = films.findLast { film ->
            film.id == filmId
        } ?: return false

        films.remove(film)
        return films.add(film.copy(dislikedBy = film.dislikedBy + userId))
    }

    override suspend fun removeDislikeFromFilm(filmId: String, userId: String): Boolean {
        val film = films.findLast { film ->
            film.id == filmId
        } ?: return false

        films.remove(film)
        return films.add(film.copy(dislikedBy = film.dislikedBy - userId))
    }

    override suspend fun getLikeCountForFilm(filmId: String): Int {
        return films.findLast { film ->
            film.id == filmId
        }?.likedBy?.size ?: 0
    }

    override suspend fun getDislikeCountForFilm(filmId: String): Int {
        return films.findLast { film ->
            film.id == filmId
        }?.dislikedBy?.size ?: 0
    }

    override suspend fun getFilmsWithGenre(genre: String): List<Film> {
        return films.filter { film -> film.imageUrls.contains(genre) }
    }

    override suspend fun getAllFilms(): List<Film> {
        return films
    }

    override suspend fun searchForFilms(query: String): List<Film> {
        return films.filter { film -> film.name.contains(query) }
    }

    override suspend fun isFilmAddedToList(filmId: String, email: String): Boolean {
        return users.findLast { user ->
            user.email == email
        }?.filmList?.contains(filmId) ?: return false
    }

    override suspend fun getNickname(email: String): String {
        return users.findLast { user ->
            user.email == email
        }?.nickname ?: return ""
    }

    override suspend fun getRandomFilms(): List<Film> {
        return listOf(films.random(), films.random(), films.random())
    }

    override suspend fun getRandomFilmsWithGenre(genre: String): List<Film> {
        return listOf(films.filter { film -> film.genres.contains(genre) }.random())
    }

    override suspend fun deleteFilmWithName(name: String): Boolean {
        return films.remove(films.findLast { film ->
            film.name == name
        })
    }

    override suspend fun getFilmIdWithName(name: String): String {
        return films.findLast { film ->
            film.name == name
        }?.id ?: ""
    }

    override suspend fun deleteUserWithEmail(email: String): Boolean {
        return users.remove(users.findLast { user ->
            user.email == email
        })
    }

    override suspend fun addWatchTime(email: String, watchTimePair: GenreWatchTimePair): Boolean {
        val user = users.findLast { user ->
            user.email == email
        } ?: return false

        val watchTimeByGenres = user.totalWatchTimeByGenre

        var pairIndex = -1
        if (watchTimeByGenres.isNotEmpty()) {
            for (i in watchTimeByGenres.indices) {
                if (watchTimeByGenres[i].genre == watchTimePair.genre) {
                    pairIndex = i
                    break
                }
            }
        }

        return if (pairIndex != -1) {
            val totalTime = watchTimeByGenres[pairIndex].totalWatchTimeInSeconds + watchTimePair.totalWatchTimeInSeconds
            val insertTimePair = GenreWatchTimePair(watchTimePair.genre, totalTime)
            users.add(user.copy(totalWatchTimeByGenre = watchTimeByGenres - watchTimeByGenres[pairIndex] + insertTimePair))
        } else {
            users.add(user.copy(totalWatchTimeByGenre = watchTimeByGenres + watchTimePair))
        }
    }

    override suspend fun getWatchTime(email: String): List<GenreWatchTimePair> {
        return users.findLast { user ->
            user.email == email
        }?.totalWatchTimeByGenre ?: emptyList()
    }

    override suspend fun getTotalFilmCount(): Int {
        return films.size
    }

    override suspend fun getTotalUserCount(): Int {
        return users.size
    }

    override suspend fun getTotalWatchStats(): List<List<GenreWatchTimePair>> {
        return users.map {
            it.totalWatchTimeByGenre
        }
    }

    override suspend fun getAllUsers(): List<User> {
        return users
    }
}