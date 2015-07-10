package de.fu_berlin.cdv.chasingpictures.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * @author Simon Kalt
 */
JsonIgnoreProperties(ignoreUnknown = true)
public class LoginApiResult : ApiResult<UserData>()
