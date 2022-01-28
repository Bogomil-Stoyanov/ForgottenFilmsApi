package eu.bbsapps.forgottenfilmsapi.data.collections

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

/**
 * A film entity that is saved in the database
 * @param name Movie title
 * @param description Movie description
 * @param imageUrls a list of image url, the first one is the main image
 * @param genres a list of genres
 * @param likedBy a list of ids of user who have like the film
 * @param dislikedBy a list of ids of user who have dislike the film
 * @param url url to the .mp4 file
 * @param id
 */
data class Movie(
    val name: String,
    val imageUrls: List<String>,
    val description: String,
    val genres: List<String>,
    val likedBy: List<String>,
    val dislikedBy: List<String>,
    val url: String,
    @BsonId val id: String = ObjectId().toString()
)