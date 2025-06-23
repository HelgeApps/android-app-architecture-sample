import java.util.Properties

plugins {
    alias(libs.plugins.my.android.library)
    alias(libs.plugins.my.android.hilt)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.arch.example.network"
    buildFeatures {
        buildConfig = true
    }
    productFlavors {
        getAt("dev").apply {
            buildConfigField("String", "BASE_URL", "\"https://api.unsplash.com\"")
        }
        getAt("prod").apply {
            buildConfigField("String", "BASE_URL", "\"https://api.unsplash.com\"")
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

secrets {
    defaultPropertiesFileName = "secrets.defaults.properties"
}

dependencies {
    api(projects.common)
    api(projects.entities)
    api(projects.datastore)
    api(projects.translations)

    // OkHttp
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    // Retrofit
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.moshi)

    // Moshi
    api(libs.moshi)
    ksp(libs.moshi.kotlin.codegen)
}

fun readProperties(propertiesFile: File) = Properties().apply {
    propertiesFile.inputStream().use { fis ->
        load(fis)
    }
}