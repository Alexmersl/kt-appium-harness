package dev.aleksejgolikov.harness.framework.adb

import dev.aleksejgolikov.harness.config.HarnessConfig
import java.util.concurrent.TimeUnit

/**
 * Host-side `adb -s <udid> shell …`. Prefer this over Appium `mobile: shell`
 * (that needs `--allow-insecure=adb_shell`).
 */
object Adb {
    fun exec(shell: String): String {
        val process =
            ProcessBuilder("adb", "-s", HarnessConfig.deviceId(), "shell", shell)
                .redirectErrorStream(true)
                .start()
        val output = process.inputStream.bufferedReader().readText()
        process.waitFor(20, TimeUnit.SECONDS)
        return output.trim()
    }

    fun setWifi(enabled: Boolean) {
        exec("svc wifi ${onOff(enabled)}")
    }

    fun setMobileData(enabled: Boolean) {
        exec("svc data ${onOff(enabled)}")
    }

    fun setAirplaneMode(enabled: Boolean) {
        exec("settings put global airplane_mode_on ${if (enabled) 1 else 0}")
        exec("am broadcast -a android.intent.action.AIRPLANE_MODE --ez state $enabled")
    }

    fun disableInternet() {
        setWifi(false)
        setMobileData(false)
    }

    fun enableInternet() {
        setAirplaneMode(false)
        setWifi(true)
        setMobileData(true)
    }

    fun isAirplaneOn(): Boolean = exec("settings get global airplane_mode_on") == "1"

    private fun onOff(enabled: Boolean) = if (enabled) "enable" else "disable"
}
