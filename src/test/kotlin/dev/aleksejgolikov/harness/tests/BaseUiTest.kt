package dev.aleksejgolikov.harness.tests

import com.codeborne.selenide.Selenide
import dev.aleksejgolikov.harness.driver.DriverFactory
import io.appium.java_client.AppiumDriver
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseUiTest {
    lateinit var session: AppiumDriver

    @BeforeAll
    fun startSession() {
        session = DriverFactory.openSession()
    }

    @AfterAll
    fun stopSession() {
        runCatching {
            if (::session.isInitialized) {
                session.quit()
            }
        }
        Selenide.closeWebDriver()
    }
}
