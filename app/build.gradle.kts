plugins {
    alias(libs.plugins.my.android.application)
    alias(libs.plugins.my.android.hilt)
}

android {
    namespace = "com.arch.example"
    defaultConfig {
        applicationId = "com.arch.example"
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "com.arch.example.testing.HiltTestRunner"
    }
    buildTypes {
        debug {
            manifestPlaceholders["screenOrientation"] = "unspecified"
            //manifestPlaceholders.crashlyticsCollectionEnabled = "false" // uncomment if you use Firebase Crashlytics
        }
        release {
            manifestPlaceholders["screenOrientation"] = "userPortrait"
            //manifestPlaceholders.crashlyticsCollectionEnabled = "true" // uncomment if you use Firebase Crashlytics
            isMinifyEnabled = true
            isShrinkResources = true
            //firebaseCrashlytics { mappingFileUploadEnabled = true } // uncomment if you use Firebase Crashlytics
        }
    }
    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
    // set name for both APK and ABB files, built type and flavor will be appended automatically
    setProperty("archivesBaseName", "${defaultConfig.applicationId}_${defaultConfig.versionName}")
}

dependencies {
    implementation(projects.feature.login)
    implementation(projects.feature.topics)
    implementation(projects.feature.photos)
    implementation(projects.feature.topicdetails)

    implementation(projects.common)
    implementation(projects.entities)
    implementation(projects.datastore)
    implementation(projects.data)
    implementation(projects.domain)
    implementation(projects.designsystem)
    implementation(projects.ui)
    implementation(projects.translations)

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)

    // Tests
    // TODO: switch to compose libs, need to rewrite tests:
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")
    androidTestImplementation(libs.hilt.android.testing)
    //kspAndroidTest(libs.hilt.compiler)
}
