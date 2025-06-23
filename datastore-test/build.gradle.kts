plugins {
    alias(libs.plugins.my.android.library)
    alias(libs.plugins.my.android.hilt)
}

android {
    namespace = "com.arch.example.datastore.test"
}

dependencies {
    implementation(libs.hilt.android.testing)
    implementation(projects.common)
    implementation(projects.datastore)
}
