import java.util.Properties

plugins {
    alias(libs.plugins.my.android.library)
    alias(libs.plugins.my.android.hilt)
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
    defaultConfig {
        val projectProperties = readProperties(file("../local.properties"))

        buildConfigField(
            type = "String",
            name = "UNSPLASH_API_ACCESS_KEY",
            value = "\"${projectProperties.getOrDefault("UNSPLASH_API_ACCESS_KEY", "") as String}\""
        )
        buildConfigField(
            type = "String",
            name = "UNSPLASH_API_SECRET_KEY",
            value = "\"${projectProperties.getOrDefault("UNSPLASH_API_SECRET_KEY", "") as String}\""
        )
    }
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