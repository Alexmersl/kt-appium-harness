package dev.aleksejgolikov.harness.tests

import dev.aleksejgolikov.harness.ui.screens.DemoHomeScreen.Companion.demoHome
import dev.aleksejgolikov.harness.ui.screens.DemoListScreen.Companion.demoList
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class DemoListTests : BaseUiTest() {
    @Test
    @DisplayName("List Demo scrolls to a late cloud row")
    fun scrollListToLateRowTest() {
        demoHome {
            assert { homeIsShown() }
            openList()
        }
        demoList {
            assert { listIsShown() }
            scrollTo("Virga")
        }
    }
}
