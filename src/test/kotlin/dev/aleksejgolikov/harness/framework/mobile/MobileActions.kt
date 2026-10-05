package dev.aleksejgolikov.harness.framework.mobile

import com.codeborne.selenide.Condition
import dev.aleksejgolikov.harness.ui.actions.Flick
import dev.aleksejgolikov.harness.ui.actions.flickAcross
import java.time.Duration

private const val SHOWN_SEC = 25L

fun MobileNode.waitShown(timeoutSec: Long = SHOWN_SEC): MobileNode =
    apply { el().shouldBe(Condition.visible, Duration.ofSeconds(timeoutSec)) }

fun MobileNode.waitHidden(timeoutSec: Long = SHOWN_SEC): MobileNode =
    apply { el().shouldNotBe(Condition.visible, Duration.ofSeconds(timeoutSec)) }

fun MobileNode.waitGone(timeoutSec: Long = SHOWN_SEC): MobileNode =
    apply { el().should(Condition.disappear, Duration.ofSeconds(timeoutSec)) }

fun MobileNode.waitEnabled(
    enabled: Boolean = true,
    timeoutSec: Long = SHOWN_SEC,
): MobileNode =
    apply {
        if (enabled) {
            el().shouldBe(Condition.enabled, Duration.ofSeconds(timeoutSec))
        } else {
            el().shouldBe(Condition.disabled, Duration.ofSeconds(timeoutSec))
        }
    }

fun MobileNode.waitHasText(
    text: String,
    timeoutSec: Long = SHOWN_SEC,
): MobileNode = apply { el().shouldHave(Condition.exactText(text), Duration.ofSeconds(timeoutSec)) }

fun MobileNode.tap(timeoutSec: Long = SHOWN_SEC): MobileNode =
    apply {
        waitShown(timeoutSec)
        el().click()
    }

fun MobileNode.type(
    text: String,
    timeoutSec: Long = SHOWN_SEC,
): MobileNode =
    apply {
        tap(timeoutSec)
        el().clear()
        el().sendKeys(text)
    }

fun MobileNode.swipe(direction: Flick): MobileNode =
    apply {
        waitShown()
        flickAcross(direction)
    }

fun MobileNode.exists(): Boolean = el().exists()
