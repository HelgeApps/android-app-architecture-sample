pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
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
include(":feature:topicdetails")
include(":feature:topics")
include(":feature:photos")

check(JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_17)) {
    """
    Now in Android requires JDK 17+ but it is currently using JDK ${JavaVersion.current()}.
    Java Home: [${System.getProperty("java.home")}]
    https://developer.android.com/build/jdks#jdk-config-in-studio
    """.trimIndent()
}
