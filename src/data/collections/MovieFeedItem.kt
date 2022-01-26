package eu.bbsapps.forgottenfilmsapi.data.collections

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

/**
 * @param name The title of the film
 * @param image The main image of the film
 */
data class MovieFeedItem(
    val name: String,
    val image: String,
    @BsonId val id: String = ObjectId().toString()
)
