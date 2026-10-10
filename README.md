# CineVerse Android source project

This is an Android Studio source project, not a prebuilt APK. It includes a GitHub Actions workflow to build a debug APK in the cloud without a PC.

## Build from an Android phone (cloud build)
1. Sign in to GitHub in your phone browser and create a **new private repository**.
2. Upload the contents of this project to the repository (extract the ZIP first; upload the files/folders inside `CineVerseAndroid`, including `.github`).
3. Open the repository's **Actions** tab, select **Build CineVerse APK**, and tap **Run workflow**. If asked, enable Actions.
4. When the run completes successfully, open that run and download the `CineVerse-debug-apk` artifact. Extract it and install the APK. Android may ask you to allow installs from your browser/files app.

The workflow also runs automatically on every push to `main` and on every pull request, so a broken change is caught before it lands.

## Project layout

```
app/
  build.gradle.kts                                  Android + Kotlin + Compose config
  src/main/AndroidManifest.xml                      app manifest
  src/main/java/com/cineverse/app/MainActivity.kt    single-activity Compose UI
build.gradle.kts                                    root script (plugin versions)
settings.gradle.kts                                 includes the :app module
gradle/wrapper/                                     Gradle wrapper (8.10.2)
.github/workflows/main.yml                          APK build workflow
```

The debug APK is written to `app/build/outputs/apk/debug/app-debug.apk` during the build.

## Important limitations
- A successful cloud build creates a debug APK; it does not prove all movie sources work.
- The supplied Cloudstream `.cs3` extensions require a compatible runtime and are not executed by this app as-is. This source does not implement a Cloudstream extension runtime or automatically provide full-length movie streams.
- Only use streams you have permission to access.
