package dev.aleksejgolikov.harness.device

import java.nio.channels.FileChannel
import java.nio.channels.FileLock
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption.CREATE
import java.nio.file.StandardOpenOption.WRITE

/**
 * One Gradle worker JVM takes one device for the whole test task.
 * Exclusive file locks so a second `./gradlew test` cannot steal a busy UDID.
 */
object DeviceLease {
    private const val APPIUM_START = 4723
    private const val APPIUM_STEP = 2

    data class Grant(
        val index: Int,
        val udid: String,
        val appiumUrl: String,
        val systemPort: Int,
        val wdaPort: Int,
    )

    private var grant: Grant? = null
    private var lock: FileLock? = null

    @Synchronized
    fun take(): Grant = grant ?: lockNext().also { publish(it) }

    private fun lockNext(): Grant {
        val ids = listedDevices()
        if (ids.isEmpty()) {
            return Grant(0, "", "http://127.0.0.1:$APPIUM_START", 8200, 8100).also { grant = it }
        }
        val dir = Paths.get("build", "device-leases")
        Files.createDirectories(dir)
        ids.forEachIndexed { index, udid ->
            val channel = FileChannel.open(dir.resolve("$index.lock"), CREATE, WRITE)
            val held = runCatching { channel.tryLock() }.getOrNull()
            if (held != null) {
                lock = held
                return Grant(
                    index = index,
                    udid = udid,
                    appiumUrl = "http://127.0.0.1:${APPIUM_START + index * APPIUM_STEP}",
                    systemPort = 8200 + index,
                    wdaPort = 8100 + index,
                ).also { grant = it }
            }
            channel.close()
        }
        error("No free device lease. Listed: ${ids.joinToString()}")
    }

    private fun listedDevices(): List<String> {
        val raw =
            sequenceOf(
                System.getProperty("devices"),
                System.getenv("DEVICES"),
                System.getProperty("device"),
                System.getenv("DEVICE"),
            ).mapNotNull { it?.trim()?.takeIf(String::isNotEmpty) }
                .firstOrNull()
                .orEmpty()
        return raw.split(",").map(String::trim).filter { it.isNotEmpty() }
    }

    private fun publish(grant: Grant) {
        if (grant.udid.isNotEmpty()) {
            System.setProperty("device", grant.udid)
        }
        System.setProperty("remote", grant.appiumUrl)
        System.setProperty("system.port", grant.systemPort.toString())
        System.setProperty("wda.port", grant.wdaPort.toString())
        println(
            "DeviceLease slot ${grant.index}: udid=${grant.udid.ifEmpty { "(default)" }} " +
                "appium=${grant.appiumUrl} systemPort=${grant.systemPort} wdaPort=${grant.wdaPort}",
        )
    }
}
