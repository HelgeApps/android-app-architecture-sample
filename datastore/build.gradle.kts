plugins {
    alias(libs.plugins.my.android.library)
    alias(libs.plugins.my.android.hilt)
    alias(libs.plugins.protobuf)
    id("kotlin-parcelize")
    id("kotlinx-serialization")
}

android {
    namespace = "com.arch.example.datastore"
    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    api(projects.entities)
    api(libs.androidx.dataStore.core)

    implementation(projects.common)

    implementation(libs.kotlinx.serialization.protobuf)

    testImplementation(projects.datastoreTest)
    testImplementation(libs.kotlinx.coroutines.test)
}