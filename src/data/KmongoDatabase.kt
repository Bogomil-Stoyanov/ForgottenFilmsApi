package eu.bbsapps.forgottenfilmsapi.data

import eu.bbsapps.forgottenfilmsapi.data.collections.Film
import eu.bbsapps.forgottenfilmsapi.data.collections.User
import eu.bbsapps.forgottenfilmsapi.data.responses.GenreWatchTimePair
import eu.bbsapps.forgottenfilmsapi.security.checkHashForPassword
import org.litote.kmongo.MongoOperator.sample
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.aggregate
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.coroutine.projection
import org.litote.kmongo.coroutine.toList
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.regex
import org.litote.kmongo.setValue

class KmongoDatabase : DataAccessObject {
    private val client = KMongo.createClient().coroutine
    private val database = client.getDatabase("ForgottenFilmsDatabase")
    private val users = database.getCollection<User>()
    private val films = database.getCollection<Film>()

    override suspend fun registerUser(user: User): Boolean {
        return users.insertOne(user).wasAcknowledged()
    }

    override suspend fun checkIfUserExists(email: String): Boolean {
        return users.findOne(User::email eq email) != null
    }

    override suspend fun updateUserPassword(email: String, newPassword: String): Boolean {
        return users.updateOne(User::email eq email, setValue(User::password, newPassword)).wasAcknowledged()
    }

    override suspend fun checkPasswordForEmail(email: String, passwordToCheck: String): Boolean {
        val actualPassword = users.findOne(User::email eq email)?.password ?: return false
        return checkHashForPassword(passwordToCheck, actualPassword)
    }

    override suspend fun changeUserNickname(email: String, newNickname: String): Boolean {
        return users.updateOne(User::email eq email, setValue(User::nickname, newNickname)).wasAcknowledged()
    }

    override suspend fun addUserGenre(email: String, genre: String): Boolean {
        val genres = users.findOne(User::email eq email)?.genres ?: return false
        if (genres.contains(genre)) return true
        return users.updateOne(User::email eq email, setValue(User::genres, genres + genre)).wasAcknowledged()
    }

    override suspend fun deleteAllUserGenres(email: String) {
        users.updateOne(User::email eq email, setValue(User::genres, emptyList()))
    }

    override suspend fun getUserGenres(email: String): List<String> {
        return users.findOne(User::email eq email)?.genres ?: emptyList()
    }

    override suspend fun addFilmToUserList(email: String, filmId: String): Boolean {
        val filmList = users.findOne(User::email eq email)?.filmList ?: return false
        if (filmList.contains(filmId)) return true
        return users.updateOne(User::email eq email, setValue(User::filmList, filmList + filmId)).wasAcknowledged()
    }

    override suspend fun removeFilmFromUserList(email: String, filmId: String): Boolean {
        val filmList = users.findOne(User::email eq email)?.filmList ?: return false
        if (!filmList.contains(filmId)) return false
        return users.updateOne(User::email eq email, setValue(User::filmList, filmList - filmId)).wasAcknowledged()
    }

    override suspend fun getUserFilmIdsList(email: String): List<String> {
        return users.findOne(User::email eq email)?.filmList?.toList() ?: emptyList()
    }

    override suspend fun addFilm(film: Film): Boolean {
        return films.insertOne(film).wasAcknowledged()
    }

    override suspend fun deleteFilmWithId(id: String): Boolean {
        return films.deleteOneById(id).wasAcknowledged()
    }

    override suspend fun getFilmWithId(id: String): Film {
        return films.findOneById(id) ?: Film(
            "null",
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
        val film = getFilmWithId(filmId)
        return film.likedBy.contains(userId)
    }

    override suspend fun isFilmDislikedBy(filmId: String, userId: String): Boolean {
        val film = getFilmWithId(filmId)
        return film.dislikedBy.contains(userId)
    }

    override suspend fun getUserIdByEmail(email: String): String {
        return users.findOne(User::email eq email)?.id ?: ""
    }

    override suspend fun addLikeToFilm(filmId: String, userId: String): Boolean {
        val film = getFilmWithId(filmId)
        return films.updateOneById(filmId, setValue(Film::likedBy, film.likedBy + userId)).wasAcknowledged()
    }

    override suspend fun removeLikeFromFilm(filmId: String, userId: String): Boolean {
        val film = getFilmWithId(filmId)
        return films.updateOneById(filmId, setValue(Film::likedBy, film.likedBy - userId)).wasAcknowledged()
    }

    override suspend fun addDislikeToFilm(filmId: String, userId: String): Boolean {
        val film = getFilmWithId(filmId)
        return films.updateOneById(filmId, setValue(Film::dislikedBy, film.dislikedBy + userId)).wasAcknowledged()
    }

    override suspend fun removeDislikeFromFilm(filmId: String, userId: String): Boolean {
        val film = getFilmWithId(filmId)
        return films.updateOneById(filmId, setValue(Film::dislikedBy, film.dislikedBy - userId)).wasAcknowledged()
    }

    override suspend fun getLikeCountForFilm(filmId: String): Int {
        return getFilmWithId(filmId).likedBy.size
    }

    override suspend fun getDislikeCountForFilm(filmId: String): Int {
        return getFilmWithId(filmId).dislikedBy.size
    }

    override suspend fun getFilmsWithGenre(genre: String): List<Film> {
        return films.find(Film::genres contains genre).toList()
    }

    override suspend fun getAllFilms(): List<Film> {
        return films.collection.find().toList()
    }

    override suspend fun searchForFilms(query: String): List<Film> {
        return films.find(Film::name regex Regex("(?i).*$query.*")).toList()
    }

    override suspend fun isFilmAddedToList(filmId: String, email: String): Boolean {
        return users.findOne(User::email eq email)?.filmList?.contains(filmId) ?: false
    }

    override suspend fun getNickname(email: String): String {
        return users.findOne(User::email eq email)?.nickname ?: ""
    }

    override suspend fun getRandomFilms(): List<Film> {
        return films.aggregate<Film>("""[{$sample:{size:5}}]""").toList()
    }

    override suspend fun getRandomFilmsWithGenre(genre: String): List<Film> {
        return films.find(Film::genres contains genre).limit(5).toList()
    }

    override suspend fun deleteFilmWithName(name: String): Boolean {
        return films.deleteOne(Film::name eq name).wasAcknowledged()
    }

    override suspend fun getFilmIdWithName(name: String): String {
        return films.findOne(Film::name eq name)?.id ?: "-1"
    }

    override suspend fun deleteUserWithEmail(email: String): Boolean {
        return users.deleteOne(User::email eq email).wasAcknowledged()
    }

    override suspend fun addWatchTime(email: String, watchTimePair: GenreWatchTimePair): Boolean {
        val watchTimeByGenres = users.findOne(User::email eq email)?.totalWatchTimeByGenre ?: emptyList()
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
            users.updateOne(
                User::email eq email,
                setValue(User::totalWatchTimeByGenre, watchTimeByGenres - watchTimeByGenres[pairIndex] + insertTimePair)
            ).wasAcknowledged()
        } else {
            users.updateOne(
                User::email eq email,
                setValue(User::totalWatchTimeByGenre, watchTimeByGenres + watchTimePair)
            ).wasAcknowledged()
        }
    }

    override suspend fun getWatchTime(email: String): List<GenreWatchTimePair> {
        return users.findOne(User::email eq email)?.totalWatchTimeByGenre ?: emptyList()
    }

    override suspend fun getTotalFilmCount(): Int {
        return films.countDocuments().toInt()
    }

    override suspend fun getTotalUserCount(): Int {
        return users.countDocuments().toInt()
    }

    override suspend fun getTotalWatchStats(): List<List<GenreWatchTimePair>> {
        return users.projection(User::totalWatchTimeByGenre).toList()
    }

    override suspend fun getAllUsers(): List<User> {
        return users.find().toList()
    }
}