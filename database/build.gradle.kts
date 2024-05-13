plugins {
    alias(libs.plugins.my.android.library)
    alias(libs.plugins.my.android.hilt)
    alias(libs.plugins.my.android.room)
}

android {
    defaultConfig {
        testInstrumentationRunner =
            "com.arch.example.testing.HiltTestRunner"
    }
    namespace = "com.arch.example.database"
}

dependencies {
    api(projects.entities)

    androidTestImplementation(projects.testing)
}