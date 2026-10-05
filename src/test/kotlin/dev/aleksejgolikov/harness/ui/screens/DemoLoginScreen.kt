package dev.aleksejgolikov.harness.ui.screens

import dev.aleksejgolikov.harness.config.HarnessConfig
import dev.aleksejgolikov.harness.config.Platform
import dev.aleksejgolikov.harness.framework.device.DeviceActions
import dev.aleksejgolikov.harness.framework.mobile.MobileQuery
import dev.aleksejgolikov.harness.framework.mobile.tap
import dev.aleksejgolikov.harness.framework.mobile.type
import dev.aleksejgolikov.harness.framework.mobile.waitShown
import dev.aleksejgolikov.harness.framework.nativeui.NativeQuery
import dev.aleksejgolikov.harness.framework.nativeui.waitShown
import io.qameta.allure.Step

class DemoLoginScreen : BaseScreen() {
    private val username = MobileQuery.accessibility("username")
    private val password = MobileQuery.tag("password")
    private val submit = MobileQuery.accessibility("loginBtn")
    private val nativeUsername = NativeQuery.desc("username")

    @Step("Submit login form")
    fun signIn(
        user: String,
        pass: String,
    ) {
        username.type(user)
        password.type(pass)
        submit.tap()
    }

    @Step("Press system back")
    fun pressSystemBack() {
        DeviceActions.pressBack()
    }

    fun assert(block: Asserts.() -> Unit): DemoLoginScreen = apply { Asserts().block() }

    inner class Asserts {
        @Step("Login fields are visible")
        fun formIsShown() {
            username.waitShown()
            password.waitShown()
            submit.waitShown()
            if (HarnessConfig.platform() == Platform.ANDROID) {
                nativeUsername.waitShown()
            }
        }

        @Step("Logged-in status is visible")
        fun loggedInAs(user: String) {
            MobileQuery.textContains("You are logged in as $user").waitShown()
        }
    }

    companion object {
        fun demoLogin(block: DemoLoginScreen.() -> Unit): DemoLoginScreen = DemoLoginScreen().apply(block)
    }
}
