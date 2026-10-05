# kt-appium-harness

Kotlin **Appium** UI test framework: Selenide + UiAutomator2/XCUITest + host ADB.

## Layers

| Layer | Entry | Locators | Actions |
|---|---|---|---|
| Mobile (Selenide/Appium) | `MobileQuery` | tag, accessibility, id, text, desc, class, xpath, UiAutomator, iOS predicate/class chain, `tagAndText`, `idWithText`, descendant/child | `tap`, `type`, `waitShown` / Hidden / Gone / Enabled / HasText, swipe, `scrollIntoViewText` |
| Native (UiAutomator2) | `NativeQuery` | res, text, desc, clazz, pkg, `resAndText`, child / fromParent | `tap`, `type`, `waitShown`, swipe, `scrollUntilFound` |
| Device | `Adb` / `scripts/adb-network.sh` | — | wifi / data / airplane on/off |

Page objects: private fields are locators; tests call screen methods only.

```kotlin
private val loginRow = MobileQuery.tag("Login Screen")
private val listByText = MobileQuery.text("List Demo")
private val nativeLogin = NativeQuery.desc("Login Screen")

loginRow.tap()
listByText.waitShown()
nativeLogin.scrollUntilFound()
Adb.disableInternet()
```

Network from a host shell (`ANDROID_SERIAL` optional):

```bash
./scripts/adb-network.sh net-off
./scripts/adb-network.sh net-on
```

## Stack

| Piece | Choice |
|---|---|
| Language | Kotlin, JVM 21 |
| Tests | JUnit 5 |
| UI | Selenide Appium, Appium Java client |
| Devices | `DeviceLease` file-lock + Gradle `maxParallelForks` |
| Config | system properties + env |
| Sample app | [The App](https://github.com/appium-pro/TheApp) (public demo APK) |

## Run locally

1. JDK 21, Appium 2 (`uiautomator2` and/or `xcuitest`), Android emulator or iOS simulator.
2. Download The App APK (or `.app`) and point `APP` at it.
3. Start Appium: `appium --port 4723` (second device: `4725`).
4. Boot the emulator (`adb devices` → e.g. `emulator-5554`).

```bash
./gradlew test -Dplatform=ANDROID \
  -Papp="$HOME/apps/TheApp.apk" \
  -Pdevices='emulator-5554'
```

Two emulators (quote the list):

```bash
./gradlew test -Dplatform=ANDROID \
  -Papp="$HOME/apps/TheApp.apk" \
  -Pdevices='emulator-5554,emulator-5556'
```

Lease ports (first id in `-Pdevices` = slot 0):

| Slot | Appium | Android systemPort | iOS WDA |
|------|--------|--------------------|---------|
| 0 | 4723 | 8200 | 8100 |
| 1 | 4725 | 8201 | 8101 |

## Layout

```
src/main/kotlin/.../config              properties → capabilities
src/main/kotlin/.../device              file-lock lease per Gradle worker
src/main/kotlin/.../driver              Android / iOS session factory
src/test/kotlin/.../framework/mobile    MobileQuery + waits / lists
src/test/kotlin/.../framework/nativeui  AppiumBy.androidUIAutomator
src/test/kotlin/.../framework/adb       host adb -s <udid> shell
src/test/kotlin/.../ui/screens          page objects (private locators)
src/test/kotlin/.../tests               JUnit only (no locators)
scripts/adb-network.sh                  wifi / data / airplane
```

## License

MIT — see `LICENSE`.
