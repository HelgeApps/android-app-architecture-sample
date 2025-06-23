plugins {
    alias(libs.plugins.my.android.library)
    alias(libs.plugins.my.android.hilt)
}

android {
    namespace = "com.arch.example.core"
}

dependencies {
    api(libs.kotlinx.coroutines.android)

    // testing
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
}