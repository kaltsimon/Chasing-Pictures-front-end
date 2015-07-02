package de.fu_berlin.cdv.chasingpictures.api

/**
 * Data class for login and registration requests.
 * Used for automatic serialization to JSON.
 *
 * @author Simon Kalt
 */

public data class LoginRequestData(
        var name: String? = null,
        var email: String? = null,
        var password: String? = null
) {
    /**
     * Constructor for a log in request
     */
    constructor(email: String, password: String) : this(null, email, password)
}