plugins {
    kotlin("jvm") version "2.2.0"
}

group = "dev.aleksejgolikov"
version = "0.1.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

val selenideAppium = "7.15.1"
val appiumClient = "9.4.0"
val junitJupiter = "5.11.4"

dependencies {
    implementation("com.codeborne:selenide-appium:$selenideAppium")
    implementation("io.appium:java-client:$appiumClient")
    implementation("org.slf4j:slf4j-simple:2.0.16")

    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiter")
    testImplementation("io.qameta.allure:allure-junit5:2.29.1")
}

tasks.test {
    useJUnitPlatform()
    val devices = deviceList()
    maxParallelForks = devices.size.coerceAtLeast(1)
    forkEvery = 0
    if (devices.isNotEmpty()) {
        systemProperty("devices", devices.joinToString(","))
    }
    resolvedPlatform()?.let {
        systemProperty("platform", it)
        systemProperty("PLATFORM", it)
    }
    resolvedApp()?.let {
        systemProperty("app", it)
        systemProperty("APP", it)
    }
    testLogging {
        showStandardStreams = true
        events("passed", "skipped", "failed")
    }
}

fun Project.deviceList(): List<String> {
    val raw =
        (findProperty("devices") as? String)?.trim().orEmpty()
            .ifEmpty { System.getenv("DEVICES").orEmpty() }
    return raw.split(",").map { it.trim() }.filter { it.isNotEmpty() }
}

fun Project.resolvedPlatform(): String? =
    listOfNotNull(
        findProperty("platform") as? String,
        System.getProperty("platform"),
        System.getenv("PLATFORM"),
    ).map { it.trim() }.firstOrNull { it.isNotEmpty() }

fun Project.resolvedApp(): String? =
    listOfNotNull(
        findProperty("app") as? String,
        System.getProperty("app"),
        System.getenv("APP"),
    ).map { it.trim() }.firstOrNull { it.isNotEmpty() }
