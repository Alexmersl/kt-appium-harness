package dev.aleksejgolikov.harness.config

enum class Platform { ANDROID, IOS }

object HarnessConfig {
    fun platform(): Platform =
        first("platform", "PLATFORM")?.uppercase().let {
            when (it) {
                "IOS" -> Platform.IOS
                else -> Platform.ANDROID
            }
        }

    fun appPath(): String =
        first("app", "APP")
            ?: error("Set -Papp= or APP to the demo .apk / .app path")

    fun deviceId(): String =
        first("device", "DEVICE") ?: "emulator-5554"

    fun remoteUrl(): String = first("remote") ?: "http://127.0.0.1:4723"

    fun systemPort(): Int = first("system.port")?.toInt() ?: 8200

    fun wdaPort(): Int = first("wda.port")?.toInt() ?: 8100

    fun appPackage(): String = first("app.package", "APP_PACKAGE") ?: "io.cloudgrey.the_app"

    fun bundleId(): String = first("bundle.id") ?: "io.cloudgrey.the-app"

    private fun first(vararg keys: String): String? =
        keys.firstNotNullOfOrNull { key ->
            System.getProperty(key)?.trim()?.takeIf { it.isNotEmpty() }
                ?: System.getenv(key)?.trim()?.takeIf { it.isNotEmpty() }
        }
}
