package dev.aleksejgolikov.harness.tests

import dev.aleksejgolikov.harness.ui.screens.DemoHomeScreen.Companion.demoHome
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class DemoHomeTests : BaseUiTest() {
    @Test
    @DisplayName("Home list shows Login and List Demo rows")
    fun homeListVisibleTest() {
        demoHome {
            assert { homeIsShown() }
        }
    }
}
