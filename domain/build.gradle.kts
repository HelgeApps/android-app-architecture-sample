plugins {
    alias(libs.plugins.my.android.library)
    alias(libs.plugins.my.android.hilt)
}

android {
    namespace = "com.arch.example.domain"
}

dependencies {
    implementation(projects.common)
    api(projects.data)
    api(projects.entities)
}