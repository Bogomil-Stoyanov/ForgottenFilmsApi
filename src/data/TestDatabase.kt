package eu.bbsapps.forgottenfilmsapi.data

import eu.bbsapps.forgottenfilmsapi.data.collections.Movie
import eu.bbsapps.forgottenfilmsapi.data.collections.User
import eu.bbsapps.forgottenfilmsapi.data.responses.GenreWatchTimePair
import eu.bbsapps.forgottenfilmsapi.security.checkHashForPassword


/**
 * Database for local tests which do not affect the production database
 */
class TestDatabase : DataAccessObject {

    private val users = mutableListOf<User>()
    private val movies = mutableListOf<Movie>()

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

    override suspend fun addMovie(movie: Movie): Boolean {
        movies.add(movie)
        return true
    }

    override suspend fun deleteMovieWithId(id: String): Boolean {
        return movies.removeAll { movie -> movie.id == id }
    }

    override suspend fun getMovieWithId(id: String): Movie {
        return movies.findLast { movie -> movie.id == id } ?: Movie(
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

    override suspend fun isMovieLikedBy(movieId: String, userId: String): Boolean {
        return movies.findLast { movie ->
            movie.id == movieId
        }?.likedBy?.contains(userId) ?: false
    }

    override suspend fun isMovieDislikedBy(movieId: String, userId: String): Boolean {
        return movies.findLast { movie ->
            movie.id == movieId
        }?.dislikedBy?.contains(userId) ?: false
    }

    override suspend fun getUserIdByEmail(email: String): String {
        return users.findLast { user -> user.email == email }?.id ?: ""
    }

    override suspend fun addLikeToMovie(movieId: String, userId: String): Boolean {
        val movie = movies.findLast { movie ->
            movie.id == movieId
        } ?: return false

        movies.remove(movie)
        return movies.add(movie.copy(likedBy = movie.likedBy + userId))
    }

    override suspend fun removeLikeFromMovie(movieId: String, userId: String): Boolean {
        val movie = movies.findLast { movie ->
            movie.id == movieId
        } ?: return false

        movies.remove(movie)
        return movies.add(movie.copy(likedBy = movie.likedBy - userId))
    }

    override suspend fun addDislikeToMovie(movieId: String, userId: String): Boolean {
        val movie = movies.findLast { movie ->
            movie.id == movieId
        } ?: return false

        movies.remove(movie)
        return movies.add(movie.copy(dislikedBy = movie.dislikedBy + userId))
    }

    override suspend fun removeDislikeFromMovie(movieId: String, userId: String): Boolean {
        val movie = movies.findLast { movie ->
            movie.id == movieId
        } ?: return false

        movies.remove(movie)
        return movies.add(movie.copy(dislikedBy = movie.dislikedBy - userId))
    }

    override suspend fun getLikeCountForMovie(movieId: String): Int {
        return movies.findLast { movie ->
            movie.id == movieId
        }?.likedBy?.size ?: 0
    }

    override suspend fun getDislikeCountForMovie(movieId: String): Int {
        return movies.findLast { movie ->
            movie.id == movieId
        }?.dislikedBy?.size ?: 0
    }

    override suspend fun getMoviesWithGenre(genre: String): List<Movie> {
        return movies.filter { movie -> movie.imageUrls.contains(genre) }
    }

    override suspend fun getAllMovies(): List<Movie> {
        return movies
    }

    override suspend fun searchForMovies(query: String): List<Movie> {
        return movies.filter { movie -> movie.name.contains(query) }
    }

    override suspend fun isMovieAddedToList(movieId: String, email: String): Boolean {
        return users.findLast { user ->
            user.email == email
        }?.filmList?.contains(movieId) ?: return false
    }

    override suspend fun getNickname(email: String): String {
        return users.findLast { user ->
            user.email == email
        }?.nickname ?: return ""
    }

    override suspend fun getRandomMovies(): List<Movie> {
        return listOf(movies.random(), movies.random(), movies.random())
    }

    override suspend fun getRandomMoviesWithGenre(genre: String): List<Movie> {
        return listOf(movies.filter { movie -> movie.genres.contains(genre) }.random())
    }

    override suspend fun deleteMovieWithName(name: String): Boolean {
        return movies.remove(movies.findLast { movie ->
            movie.name == name
        })
    }

    override suspend fun getMovieIdWithName(name: String): String {
        return movies.findLast { movie ->
            movie.name == name
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

    override suspend fun getTotalMovieCount(): Int {
        return movies.size
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