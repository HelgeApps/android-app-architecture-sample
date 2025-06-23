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
    implementation(libs.androidx.navigation.compose)
}
