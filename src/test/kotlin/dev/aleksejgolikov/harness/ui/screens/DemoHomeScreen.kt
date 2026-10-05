package dev.aleksejgolikov.harness.ui.screens

import dev.aleksejgolikov.harness.config.HarnessConfig
import dev.aleksejgolikov.harness.config.Platform
import dev.aleksejgolikov.harness.framework.mobile.MobileQuery
import dev.aleksejgolikov.harness.framework.mobile.tap
import dev.aleksejgolikov.harness.framework.mobile.waitShown
import dev.aleksejgolikov.harness.framework.nativeui.NativeQuery
import dev.aleksejgolikov.harness.framework.nativeui.waitShown
import io.qameta.allure.Step

class DemoHomeScreen : BaseScreen() {
    private val loginRow = MobileQuery.tag("Login Screen")
    private val listRow = MobileQuery.accessibility("List Demo")
    private val loginByText = MobileQuery.text("Login Screen")
    private val listByCombined = MobileQuery.accessibilityWithText("List Demo", "List Demo")
    private val nativeLoginRow = NativeQuery.desc("Login Screen")

    @Step("Open Login Screen")
    fun openLogin() {
        loginRow.tap()
    }

    @Step("Open List Demo")
    fun openList() {
        listRow.tap()
    }

    fun assert(block: Asserts.() -> Unit): DemoHomeScreen = apply { Asserts().block() }

    inner class Asserts {
        @Step("Home list is visible")
        fun homeIsShown() {
            loginRow.waitShown()
            listRow.waitShown()
            loginByText.waitShown()
            listByCombined.waitShown()
            if (HarnessConfig.platform() == Platform.ANDROID) {
                nativeLoginRow.waitShown()
            }
        }
    }

    companion object {
        fun demoHome(block: DemoHomeScreen.() -> Unit): DemoHomeScreen = DemoHomeScreen().apply(block)
    }
}
