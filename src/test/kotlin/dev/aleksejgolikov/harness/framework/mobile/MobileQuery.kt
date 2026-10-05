package dev.aleksejgolikov.harness.framework.mobile

import com.codeborne.selenide.Selenide
import com.codeborne.selenide.SelenideElement
import dev.aleksejgolikov.harness.config.HarnessConfig
import dev.aleksejgolikov.harness.config.Platform
import io.appium.java_client.AppiumBy
import org.openqa.selenium.By
import org.openqa.selenium.support.pagefactory.ByAll
import org.openqa.selenium.support.pagefactory.ByChained

class MobileNode(
    internal val locator: By,
) {
    fun el(): SelenideElement = Selenide.element(locator)

    fun and(other: MobileNode) = MobileNode(ByChained(locator, other.locator))

    fun or(other: MobileNode) = MobileNode(ByAll(locator, other.locator))

    fun descendant(child: MobileNode) = MobileNode(ByChained(locator, child.locator))

    fun child(child: MobileNode) = descendant(child)
}

object MobileQuery {
    /** RN `testID` / accessibility id. */
    fun tag(value: String) = accessibility(value)

    fun accessibility(id: String) = MobileNode(AppiumBy.accessibilityId(id))

    fun id(resourceId: String) = MobileNode(AppiumBy.id(resourceId))

    fun className(name: String) = MobileNode(AppiumBy.className(name))

    fun xpath(expression: String) = MobileNode(AppiumBy.xpath(expression))

    fun text(value: String) =
        MobileNode(
            when (HarnessConfig.platform()) {
                Platform.ANDROID -> AppiumBy.androidUIAutomator(uiText(value, contains = false))
                Platform.IOS -> AppiumBy.iOSNsPredicateString(iosLabel(value, contains = false))
            },
        )

    fun textContains(value: String) =
        MobileNode(
            when (HarnessConfig.platform()) {
                Platform.ANDROID -> AppiumBy.androidUIAutomator(uiText(value, contains = true))
                Platform.IOS -> AppiumBy.iOSNsPredicateString(iosLabel(value, contains = true))
            },
        )

    fun desc(value: String) =
        MobileNode(
            when (HarnessConfig.platform()) {
                Platform.ANDROID ->
                    AppiumBy.androidUIAutomator("new UiSelector().description(${quote(value)})")
                Platform.IOS -> AppiumBy.iOSNsPredicateString("name == ${quote(value)} OR label == ${quote(value)}")
            },
        )

    fun descContains(value: String) =
        MobileNode(
            when (HarnessConfig.platform()) {
                Platform.ANDROID ->
                    AppiumBy.androidUIAutomator("new UiSelector().descriptionContains(${quote(value)})")
                Platform.IOS ->
                    AppiumBy.iOSNsPredicateString("name CONTAINS ${quote(value)} OR label CONTAINS ${quote(value)}")
            },
        )

    fun uiAutomator(selector: String) = MobileNode(AppiumBy.androidUIAutomator(selector))

    fun iosPredicate(predicate: String) = MobileNode(AppiumBy.iOSNsPredicateString(predicate))

    fun iosClassChain(chain: String) = MobileNode(AppiumBy.iOSClassChain(chain))

    fun instance(index: Int) =
        MobileNode(
            when (HarnessConfig.platform()) {
                Platform.ANDROID -> AppiumBy.androidUIAutomator("new UiSelector().instance($index)")
                Platform.IOS -> AppiumBy.iOSClassChain("**/*[$index]")
            },
        )

    fun tagAndText(
        tag: String,
        text: String,
    ) = accessibilityAndText(tag, text)

    fun textUnderTag(
        parentTag: String,
        text: String,
    ) = textUnderAccessibility(parentTag, text)

    fun accessibilityAndText(
        accessibilityId: String,
        text: String,
    ) = accessibility(accessibilityId).descendant(text(text))

    fun accessibilityWithText(
        accessibilityId: String,
        text: String,
    ) = MobileNode(
        when (HarnessConfig.platform()) {
            Platform.ANDROID ->
                AppiumBy.androidUIAutomator(
                    "new UiSelector().description(${quote(accessibilityId)}).text(${quote(text)})",
                )
            Platform.IOS ->
                AppiumBy.iOSNsPredicateString(
                    "name == ${quote(accessibilityId)} AND (label == ${quote(text)} OR value == ${quote(text)})",
                )
        },
    )

    fun idAndText(
        resourceId: String,
        text: String,
    ) = id(resourceId).descendant(text(text))

    fun idWithText(
        resourceId: String,
        text: String,
    ) = MobileNode(
        when (HarnessConfig.platform()) {
            Platform.ANDROID ->
                AppiumBy.androidUIAutomator(
                    "new UiSelector().resourceId(${quote(resourceId)}).text(${quote(text)})",
                )
            Platform.IOS ->
                AppiumBy.iOSNsPredicateString("name == ${quote(resourceId)} AND label == ${quote(text)}")
        },
    )

    fun idAndDesc(
        resourceId: String,
        description: String,
    ) = id(resourceId).descendant(desc(description))

    fun textAndDesc(
        text: String,
        description: String,
    ) = this.text(text).and(desc(description))

    fun textAndClass(
        text: String,
        className: String,
    ) = MobileNode(
        when (HarnessConfig.platform()) {
            Platform.ANDROID ->
                AppiumBy.androidUIAutomator(
                    "new UiSelector().className(${quote(className)}).text(${quote(text)})",
                )
            Platform.IOS ->
                AppiumBy.iOSNsPredicateString("type == ${quote(className)} AND label == ${quote(text)}")
        },
    )

    fun textUnderAccessibility(
        parentAccessibility: String,
        text: String,
    ) = accessibility(parentAccessibility).descendant(text(text))

    fun textUnderId(
        parentId: String,
        text: String,
    ) = id(parentId).descendant(text(text))

    fun idUnderId(
        parentId: String,
        childId: String,
    ) = id(parentId).descendant(id(childId))

    private fun uiText(
        value: String,
        contains: Boolean,
    ): String {
        val method = if (contains) "textContains" else "text"
        return "new UiSelector().$method(${quote(value)})"
    }

    private fun iosLabel(
        value: String,
        contains: Boolean,
    ): String =
        if (contains) {
            "label CONTAINS ${quote(value)} OR name CONTAINS ${quote(value)} OR value CONTAINS ${quote(value)}"
        } else {
            "label == ${quote(value)} OR name == ${quote(value)} OR value == ${quote(value)}"
        }

    private fun quote(value: String): String = "\"${value.replace("\"", "\\\"")}\""
}
