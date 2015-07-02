package de.fu_berlin.cdv.chasingpictures

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View


/**
 * This activity presents a page where the user can choose to
 * either log in or register a new account.

 * @author Simon Kalt
 */
public class LoginPage : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
    }

    /**
     * On tapping the log in button, go to the log in form.
     */
    public fun showLoginForm(view: View) {
        val intent = Intent(this, javaClass<LoginForm>())
        startActivityForResult(intent, MainActivity.REQUEST_LOGIN_REGISTER)
    }

    /**
     * On tapping the register button, go to the registration form.
     */
    public fun showRegisterForm(view: View) {
        val intent = Intent(this, javaClass<Register>())
        startActivityForResult(intent, MainActivity.REQUEST_LOGIN_REGISTER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // If the sub-activity says that log in/registration succeeded, pass on that result
        if (resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK)
            finish()
        }

        // Otherwise, stay here and let the user decide what to do
    }
}
