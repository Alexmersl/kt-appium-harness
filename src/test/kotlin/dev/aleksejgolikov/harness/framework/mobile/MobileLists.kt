package dev.aleksejgolikov.harness.framework.mobile

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Selenide
import dev.aleksejgolikov.harness.config.HarnessConfig
import dev.aleksejgolikov.harness.config.Platform
import dev.aleksejgolikov.harness.ui.actions.Flick
import dev.aleksejgolikov.harness.ui.actions.flickAcross
import io.appium.java_client.AppiumBy
import java.time.Duration

fun MobileNode.scrollIntoViewText(text: String): MobileNode {
    when (HarnessConfig.platform()) {
        Platform.ANDROID -> {
            val escaped = text.replace("\"", "\\\"")
            Selenide.element(
                AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().text(\"$escaped\"))",
                ),
            ).shouldBe(Condition.visible, Duration.ofSeconds(25))
        }
        Platform.IOS -> {
            val deadline = System.currentTimeMillis() + 25_000
            while (System.currentTimeMillis() < deadline) {
                val node = MobileQuery.text(text).el()
                if (node.exists() && node.`is`(Condition.visible)) return this
                flickAcross(Flick.UP)
            }
            MobileQuery.text(text).waitShown()
        }
    }
    return this
}

fun MobileNode.scrollIntoViewAccessibility(id: String): MobileNode {
    when (HarnessConfig.platform()) {
        Platform.ANDROID -> {
            val escaped = id.replace("\"", "\\\"")
            Selenide.element(
                AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().description(\"$escaped\"))",
                ),
            ).shouldBe(Condition.visible, Duration.ofSeconds(25))
        }
        Platform.IOS -> {
            val deadline = System.currentTimeMillis() + 25_000
            while (System.currentTimeMillis() < deadline) {
                val node = MobileQuery.accessibility(id).el()
                if (node.exists() && node.`is`(Condition.visible)) return this
                flickAcross(Flick.UP)
            }
            MobileQuery.accessibility(id).waitShown()
        }
    }
    return this
}

fun MobileNode.scrollToItemWithText(text: String): MobileNode = scrollIntoViewText(text)
