pluginManagement { repositories { google(); mavenCentral(); gradlePluginPortal() } }
dependencyResolutionManagement { repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS); repositories { google(); mavenCentral() } }
rootProject.name = "CineVerse"

// নূর (Noor) — দুআ ও আমল : the main app of this repository.
include(":noor")

// CineVerse (legacy source project) is kept for reference. Its APK build has its
// own workflow (.github/workflows/cineverse-apk.yml) so the two apps never
// block each other in CI.
include(":app")
