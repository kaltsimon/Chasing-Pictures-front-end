package de.fu_berlin.cdv.chasingpictures

import android.app.Activity
import android.os.Bundle
import android.util.Patterns
import android.view.View

import de.fu_berlin.cdv.chasingpictures.api.LoginRequest
import de.fu_berlin.cdv.chasingpictures.api.LoginRequestTask
import de.fu_berlin.cdv.chasingpictures.security.Access

import kotlinx.android.synthetic.activity_login_form.*

public class LoginForm : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_form)
    }

    /**
     * Perform the actual log in, if the input is acceptable.
     */
    public fun doLogin(view: View) {
        // Retrieve contents
        val emailString = LoginEmailAddress.getText().toString()
        var passwordString = LoginPassword.getText().toString()

        // Check if E-Mail address is valid
        if (!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            LoginEmailAddress.setError(getString(R.string.invalid_email))
            return
        }

        // Check if password is not empty
        if (passwordString.isEmpty()) {
            LoginPassword.setError(getString(R.string.empty_password))
            return
        }

        passwordString = Access.saltAndHash(this, passwordString)

        val loginRequest = LoginRequest.makeLoginRequest(this, emailString, passwordString)
        val loginRequestTask = LoginRequestTask.makeLoginTask(this)
        loginRequestTask.execute(loginRequest)
    }
}
