plugins {
    alias(libs.plugins.my.android.library)
    alias(libs.plugins.my.android.library.compose)
}

android {
    namespace = "com.arch.example.ui"
}

dependencies {
    implementation(projects.designsystem)
    implementation(projects.entities)
    implementation(projects.translations)
    
    // Navigation Compose
    api(libs.androidx.navigation.compose)

    // Hilt Compose
    api(libs.androidx.hilt.navigation.compose)
}
