package dev.aleksejgolikov.harness.framework.device

import com.codeborne.selenide.WebDriverRunner
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.nativekey.AndroidKey
import io.appium.java_client.android.nativekey.KeyEvent

object DeviceActions {
    fun pressBack() {
        val driver = WebDriverRunner.getWebDriver()
        when (driver) {
            is AndroidDriver -> driver.pressKey(KeyEvent(AndroidKey.BACK))
            else -> driver.navigate().back()
        }
    }

    fun currentPackage(): String? = (WebDriverRunner.getWebDriver() as? AndroidDriver)?.currentPackage
}
