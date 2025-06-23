
plugins {
    alias(libs.plugins.my.android.library)
    alias(libs.plugins.my.android.library.compose)
    alias(libs.plugins.my.android.hilt)
}

android {
    namespace = "com.arch.example.testing"
}

dependencies {
    api(kotlin("test"))
    api(libs.androidx.compose.ui.test)
    api(projects.data)
    api(projects.entities)
    api(libs.mockito.kotlin)

    debugApi(libs.androidx.compose.ui.testManifest)

    implementation(libs.androidx.test.rules)
    implementation(libs.hilt.android.testing)
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.kotlinx.datetime)
    implementation(projects.common)
    implementation(projects.designsystem)
}
