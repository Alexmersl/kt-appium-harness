package dev.aleksejgolikov.harness.tests

import dev.aleksejgolikov.harness.ui.screens.DemoHomeScreen.Companion.demoHome
import dev.aleksejgolikov.harness.ui.screens.DemoLoginScreen.Companion.demoLogin
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class DemoLoginTests : BaseUiTest() {
    @Test
    @DisplayName("Login row opens the form; alice can sign in")
    fun openLoginFormAndSignInTest() {
        demoHome {
            assert { homeIsShown() }
            openLogin()
        }
        demoLogin {
            assert { formIsShown() }
            signIn(user = "alice", pass = "mypassword")
            assert { loggedInAs("alice") }
        }
    }
}
