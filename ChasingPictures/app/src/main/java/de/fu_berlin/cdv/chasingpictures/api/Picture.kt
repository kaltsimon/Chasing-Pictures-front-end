package de.fu_berlin.cdv.chasingpictures.api

import android.util.Log
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.File
import java.io.Serializable
import java.net.URL
import java.util.Date

/**
 * This class represents a picture which is associated with a place.
 *
 * Note: If we use @JsonProperty annotations on our data class fields,
 * Kotlin places them in the automatically generated constructor. This
 * confuses Jackson which then wants an annotation on every single field.
 *
 * Solution: Place a @JsonProperty annotation on every field - As soon as we can specify
 * where annotations go, remove the ones we don't need.
 *
 * @author Simon Kalt
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public data class Picture(
        @JsonProperty("id") var id: Long = 0,
        @JsonProperty("time") var time: Date? = null,
        @JsonProperty("url") var url: String? = null,
        @JsonProperty("place_id") var placeId: Long = 0,
        @JsonProperty("created_at") var createdAt: Date? = null,
        @JsonProperty("updated_at") var updatedAt: Date? = null,
        @JsonProperty("cached_file", access = JsonProperty.Access.WRITE_ONLY) @JsonIgnore var cachedFile: File? = null,
        @JsonProperty("file_file_name") var file_file_name: String? = null,
        @JsonProperty("file_content_type") var file_content_type: String? = null,
        @JsonProperty("file_file_size") var file_file_size: Int = 0,
        @JsonProperty("file_updated_at") var file_updated_at: Date? = null,
        @JsonProperty("user_id") var user_id: Int = 0
) : Serializable {

    @JsonIgnore
    public fun getASCIIUrl(): URL? {
        try {
            val uri = URL(url).toURI()
            return URL(uri.toASCIIString())
        } catch (e: Exception) {
            Log.e("Picture", "Error while converting URL", e)
        }
        return null
    }
}