package dev.aleksejgolikov.harness.driver

import com.codeborne.selenide.WebDriverRunner
import dev.aleksejgolikov.harness.config.HarnessConfig
import dev.aleksejgolikov.harness.config.Platform
import dev.aleksejgolikov.harness.device.DeviceLease
import io.appium.java_client.AppiumDriver
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.options.UiAutomator2Options
import io.appium.java_client.ios.IOSDriver
import io.appium.java_client.ios.options.XCUITestOptions
import java.net.URI
import java.time.Duration

object DriverFactory {
    fun openSession(): AppiumDriver {
        DeviceLease.take()
        val remote = URI(HarnessConfig.remoteUrl()).toURL()
        val driver =
            when (HarnessConfig.platform()) {
                Platform.ANDROID -> AndroidDriver(remote, androidOptions())
                Platform.IOS -> IOSDriver(remote, iosOptions())
            }
        WebDriverRunner.setWebDriver(driver)
        return driver
    }

    private fun androidOptions() =
        UiAutomator2Options().apply {
            setPlatformName("Android")
            setAutomationName("UiAutomator2")
            setApp(HarnessConfig.appPath())
            setAppPackage(HarnessConfig.appPackage())
            setUdid(HarnessConfig.deviceId())
            setSystemPort(HarnessConfig.systemPort())
            setNoReset(true)
            setNewCommandTimeout(Duration.ofMinutes(5))
        }

    private fun iosOptions() =
        XCUITestOptions().apply {
            setPlatformName("iOS")
            setAutomationName("XCUITest")
            setApp(HarnessConfig.appPath())
            setBundleId(HarnessConfig.bundleId())
            setUdid(HarnessConfig.deviceId())
            setWdaLocalPort(HarnessConfig.wdaPort())
            setNoReset(true)
            setNewCommandTimeout(Duration.ofMinutes(5))
        }
}
