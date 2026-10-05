package dev.aleksejgolikov.harness.framework.nativeui

import com.codeborne.selenide.Selenide
import com.codeborne.selenide.SelenideElement
import io.appium.java_client.AppiumBy
import org.openqa.selenium.By

/**
 * Android UiAutomator2 locators via Appium (`AppiumBy.androidUIAutomator`).
 * iOS counterpart: [dev.aleksejgolikov.harness.framework.mobile.MobileQuery.iosPredicate].
 */
class NativeNode(
    internal val locator: By,
    internal val uiSelector: String,
) {
    fun el(): SelenideElement = Selenide.element(locator)

    fun child(other: NativeNode) = NativeQuery.uiSelector("$uiSelector.childSelector(${other.uiSelector})")

    fun fromParent(other: NativeNode) = NativeQuery.uiSelector("$uiSelector.fromParent(${other.uiSelector})")
}

object NativeQuery {
    fun uiSelector(expression: String) = NativeNode(AppiumBy.androidUIAutomator(expression), expression)

    fun res(resourceId: String) = uiSelector("new UiSelector().resourceId(${quote(resourceId)})")

    fun text(value: String) = uiSelector("new UiSelector().text(${quote(value)})")

    fun textContains(value: String) = uiSelector("new UiSelector().textContains(${quote(value)})")

    fun desc(value: String) = uiSelector("new UiSelector().description(${quote(value)})")

    fun descContains(value: String) = uiSelector("new UiSelector().descriptionContains(${quote(value)})")

    fun clazz(className: String) = uiSelector("new UiSelector().className(${quote(className)})")

    fun pkg(packageName: String) = uiSelector("new UiSelector().packageName(${quote(packageName)})")

    fun instance(index: Int) = uiSelector("new UiSelector().instance($index)")

    fun scrollable() = uiSelector("new UiSelector().scrollable(true)")

    fun resAndText(
        resourceId: String,
        text: String,
    ) = uiSelector(
        "new UiSelector().resourceId(${quote(resourceId)}).text(${quote(text)})",
    )

    fun resAndDesc(
        resourceId: String,
        description: String,
    ) = uiSelector(
        "new UiSelector().resourceId(${quote(resourceId)}).description(${quote(description)})",
    )

    fun textAndClazz(
        text: String,
        className: String,
    ) = uiSelector(
        "new UiSelector().className(${quote(className)}).text(${quote(text)})",
    )

    fun pkgAndRes(
        packageName: String,
        resourceId: String,
    ) = uiSelector(
        "new UiSelector().packageName(${quote(packageName)}).resourceId(${quote(resourceId)})",
    )

    private fun quote(value: String): String = "\"${value.replace("\"", "\\\"")}\""
}
