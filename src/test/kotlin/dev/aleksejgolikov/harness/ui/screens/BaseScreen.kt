package dev.aleksejgolikov.harness.ui.screens

import com.codeborne.selenide.WebDriverRunner
import io.appium.java_client.pagefactory.AppiumFieldDecorator
import org.openqa.selenium.support.PageFactory
import java.time.Duration

open class BaseScreen {
    init {
        PageFactory.initElements(
            AppiumFieldDecorator(WebDriverRunner.getWebDriver(), Duration.ofSeconds(8)),
            this,
        )
    }
}
