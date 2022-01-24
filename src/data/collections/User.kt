package eu.bbsapps.forgottenfilmsapi.data.collections

import eu.bbsapps.forgottenfilmsapi.data.responses.GenreWatchTimePair
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

/**
 * A user entity that is saved in the database
 * @param email user's email
 * @param password user's password
 * @param nickname user's nickname
 * @param genres a list of user's favourite genres
 * @param totalWatchTimeByGenre a list of [GenreWatchTimePair] that contains the genre and its watch time in SECONDS
 * @param filmList user's watch later list
 */
data class User(
    val email: String,
    val password: String,
    val nickname: String,
    val genres: List<String>,
    val totalWatchTimeByGenre: List<GenreWatchTimePair>,
    val filmList: List<String>,
    @BsonId val id: String = ObjectId().toString()
)
