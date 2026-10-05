package dev.aleksejgolikov.harness.framework.nativeui

import com.codeborne.selenide.Condition
import dev.aleksejgolikov.harness.ui.actions.Flick
import dev.aleksejgolikov.harness.ui.actions.flickAcross
import java.time.Duration

private const val SHOWN_SEC = 25L

fun NativeNode.waitShown(timeoutSec: Long = SHOWN_SEC): NativeNode =
    apply { el().shouldBe(Condition.visible, Duration.ofSeconds(timeoutSec)) }

fun NativeNode.waitHidden(timeoutSec: Long = SHOWN_SEC): NativeNode =
    apply { el().shouldNotBe(Condition.visible, Duration.ofSeconds(timeoutSec)) }

fun NativeNode.waitHasText(
    text: String,
    timeoutSec: Long = SHOWN_SEC,
): NativeNode = apply { el().shouldHave(Condition.exactText(text), Duration.ofSeconds(timeoutSec)) }

fun NativeNode.tap(timeoutSec: Long = SHOWN_SEC): NativeNode =
    apply {
        waitShown(timeoutSec)
        el().click()
    }

fun NativeNode.type(
    text: String,
    timeoutSec: Long = SHOWN_SEC,
): NativeNode =
    apply {
        tap(timeoutSec)
        el().clear()
        el().sendKeys(text)
    }

fun NativeNode.swipe(direction: Flick): NativeNode =
    apply {
        waitShown()
        flickAcross(direction)
    }

fun NativeNode.scrollUntilFound(
    timeoutSec: Long = SHOWN_SEC,
    direction: Flick = Flick.UP,
): NativeNode {
    val deadline = System.currentTimeMillis() + timeoutSec * 1_000
    while (System.currentTimeMillis() < deadline) {
        if (el().exists() && el().`is`(Condition.visible)) return this
        flickAcross(direction)
    }
    waitShown(2)
    return this
}

fun NativeNode.exists(): Boolean = el().exists()
