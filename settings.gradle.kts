pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://www.jitpack.io" ) }
    }
}

rootProject.name = "android-app-architecture"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":common")
include(":network")
include(":entities")
include(":datastore")
include(":data")
include(":database")
include(":designsystem")
include(":ui")
include(":domain")
include(":translations")
include(":datastore-test")
include(":testing")
include(":feature:login")
include(":feature:topicDetails")
include(":feature:topics")
include(":feature:photos")
