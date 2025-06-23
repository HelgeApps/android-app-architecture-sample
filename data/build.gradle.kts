plugins {
    alias(libs.plugins.my.android.library)
    alias(libs.plugins.my.android.hilt)
}

android {
    namespace = "com.arch.example.data"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    api(projects.common)
    api(projects.datastore)
    api(projects.network)
    api(projects.database)

    // Pagination
    api(libs.androidx.paging.compose)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotlinx.serialization.json)
    testImplementation(projects.datastoreTest)
    testImplementation(projects.testing)
}