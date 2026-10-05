package dev.aleksejgolikov.harness.ui.screens

import dev.aleksejgolikov.harness.config.HarnessConfig
import dev.aleksejgolikov.harness.config.Platform
import dev.aleksejgolikov.harness.framework.mobile.MobileNode
import dev.aleksejgolikov.harness.framework.mobile.MobileQuery
import dev.aleksejgolikov.harness.framework.mobile.scrollIntoViewText
import dev.aleksejgolikov.harness.framework.mobile.waitShown
import dev.aleksejgolikov.harness.framework.nativeui.NativeQuery
import dev.aleksejgolikov.harness.framework.nativeui.scrollUntilFound
import io.qameta.allure.Step

class DemoListScreen : BaseScreen() {
    private val list: MobileNode
        get() =
            when (HarnessConfig.platform()) {
                Platform.ANDROID -> MobileQuery.uiAutomator("new UiSelector().scrollable(true)")
                Platform.IOS -> MobileQuery.iosClassChain("**/XCUIElementTypeTable")
            }

    private fun row(title: String) = MobileQuery.text(title)

    private fun nativeRow(title: String) = NativeQuery.text(title)

    @Step("Scroll list until row is visible")
    fun scrollTo(title: String) {
        list.scrollIntoViewText(title)
        row(title).waitShown()
        if (HarnessConfig.platform() == Platform.ANDROID) {
            nativeRow(title).scrollUntilFound()
        }
    }

    fun assert(block: Asserts.() -> Unit): DemoListScreen = apply { Asserts().block() }

    inner class Asserts {
        @Step("Cloud list is visible")
        fun listIsShown() {
            list.waitShown()
            row("Altocumulus").waitShown()
        }
    }

    companion object {
        fun demoList(block: DemoListScreen.() -> Unit): DemoListScreen = DemoListScreen().apply(block)
    }
}
