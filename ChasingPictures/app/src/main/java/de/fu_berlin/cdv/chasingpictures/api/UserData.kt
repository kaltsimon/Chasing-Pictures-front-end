package de.fu_berlin.cdv.chasingpictures.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable
import java.util.Date

/**
 * Class that holds data about the user of the app.
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
public class UserData(
        @JsonProperty("id") var id: Long = 0,
        @JsonProperty("provider") var provider: String? = null,
        @JsonProperty("uid") var uid: String? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("nickname") var nickname: String? = null,
        @JsonProperty("image") var image: String? = null,
        @JsonProperty("email") var email: String? = null,
        @JsonProperty("created_at") var createdAt: Date? = null,
        @JsonProperty("updated_at") var updatedAt: Date? = null
) : Serializable {

}
