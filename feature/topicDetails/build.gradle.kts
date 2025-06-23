plugins {
    alias(libs.plugins.my.android.feature)
    alias(libs.plugins.my.android.library.compose)
}

android {
    namespace = "com.arch.example.topicdetails"
}

dependencies {
    implementation(projects.data)
    implementation(projects.domain)
    implementation(projects.translations)

    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.robolectric)
    testImplementation(projects.testing)

    androidTestImplementation(projects.testing)
}