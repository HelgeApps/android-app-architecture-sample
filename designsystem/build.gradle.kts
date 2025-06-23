plugins {
    alias(libs.plugins.my.android.library)
    alias(libs.plugins.my.android.library.compose)
}

android {
    namespace = "com.arch.example.designsystem"
}

dependencies {
    implementation(projects.translations)

    // Compose:
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.paging.compose)

    api(libs.androidx.lifecycle.runtimeCompose)

    // Coil
    api(libs.coil.kt.compose)
}
