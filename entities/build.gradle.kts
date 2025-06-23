plugins {
    alias(libs.plugins.my.android.library)
    id("kotlin-parcelize")
    id("kotlinx-serialization")
}

android {
    namespace = "com.arch.example.entities"
}

dependencies {
    implementation(libs.kotlinx.serialization.protobuf)
}