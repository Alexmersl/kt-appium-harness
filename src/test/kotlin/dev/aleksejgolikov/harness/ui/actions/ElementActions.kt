package dev.aleksejgolikov.harness.ui.actions

import com.codeborne.selenide.Condition
import com.codeborne.selenide.SelenideElement
import com.codeborne.selenide.WebDriverRunner
import io.appium.java_client.AppiumDriver
import org.openqa.selenium.interactions.PointerInput
import org.openqa.selenium.interactions.Sequence
import java.time.Duration

private const val SHOWN_SEC = 25L

fun SelenideElement.tapWhenShown(timeoutSec: Long = SHOWN_SEC) {
    shouldBe(Condition.visible, Duration.ofSeconds(timeoutSec)).click()
}

fun SelenideElement.typeWhenShown(
    text: String,
    timeoutSec: Long = SHOWN_SEC,
) {
    tapWhenShown(timeoutSec)
    clear()
    sendKeys(text)
}

fun SelenideElement.mustBeShown(
    shown: Boolean = true,
    timeoutSec: Long = SHOWN_SEC,
) {
    if (shown) {
        shouldBe(Condition.visible, Duration.ofSeconds(timeoutSec))
    } else {
        shouldNotBe(Condition.visible, Duration.ofSeconds(timeoutSec))
    }
}

fun SelenideElement.mustHaveText(
    expected: String,
    timeoutSec: Long = SHOWN_SEC,
) {
    shouldHave(Condition.exactText(expected), Duration.ofSeconds(timeoutSec))
}

enum class Flick { UP, DOWN, LEFT, RIGHT }

fun flickAcross(direction: Flick, percent: Double = 0.7) {
    val driver = WebDriverRunner.getWebDriver() as AppiumDriver
    val size = driver.manage().window().size
    val midX = size.width / 2
    val midY = size.height / 2
    val dx = (size.width * percent / 2).toInt()
    val dy = (size.height * percent / 2).toInt()
    val (fromX, fromY, toX, toY) =
        when (direction) {
            Flick.UP -> listOf(midX, midY + dy, midX, midY - dy)
            Flick.DOWN -> listOf(midX, midY - dy, midX, midY + dy)
            Flick.LEFT -> listOf(midX + dx, midY, midX - dx, midY)
            Flick.RIGHT -> listOf(midX - dx, midY, midX + dx, midY)
        }
    val finger = PointerInput(PointerInput.Kind.TOUCH, "finger")
    val swipe =
        Sequence(finger, 0)
            .addAction(
                finger.createPointerMove(
                    Duration.ZERO,
                    PointerInput.Origin.viewport(),
                    fromX,
                    fromY,
                ),
            )
            .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
            .addAction(
                finger.createPointerMove(
                    Duration.ofMillis(280),
                    PointerInput.Origin.viewport(),
                    toX,
                    toY,
                ),
            )
            .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()))
    driver.perform(listOf(swipe))
}
