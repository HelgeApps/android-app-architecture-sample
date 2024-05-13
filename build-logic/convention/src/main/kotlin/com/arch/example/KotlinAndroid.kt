package com.arch.example

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = AndroidSdkVersions.COMPILE

        defaultConfig {
            minSdk = AndroidSdkVersions.MIN
        }
        buildTypes {
            getByName("release") {
                proguardFiles(
                    getDefaultProguardFile(("proguard-android-optimize.txt")),
                    "proguard-rules.pro"
                )
            }
        }
        compileOptions {
            sourceCompatibility = AndroidSdkVersions.JAVA_VERSION
            targetCompatibility = AndroidSdkVersions.JAVA_VERSION
            isCoreLibraryDesugaringEnabled = true
        }

        kotlinOptions {
            // Treat all Kotlin warnings as errors (disabled by default)
            // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
            val warningsAsErrors: String? by project
            allWarningsAsErrors = warningsAsErrors.toBoolean()

            freeCompilerArgs = freeCompilerArgs + listOf(
                "-opt-in=kotlin.RequiresOptIn",
                // Enable experimental Kotlin Serialization, Coroutines, Flow APIs and Compose APIs
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
                "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
                "-opt-in=kotlin.Experimental",
                "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
                "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
                "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                "-opt-in=androidx.compose.runtime.ExperimentalComposeApi",
                "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi",
                "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
            )

            // Set JVM target to 17
            jvmTarget = JavaVersion.VERSION_17.toString()
        }

        packaging {
            resources {
                excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            }
        }

        // Enable/Disable build features
        // https://developer.android.com/studio/releases/gradle-plugin#buildFeatures
        buildFeatures {
            buildConfig = true
        }

        val serverFlavour = "server"
        flavorDimensions += serverFlavour
        productFlavors {
            create("dev") {
                dimension = serverFlavour
            }
            create("prod") {
                dimension = serverFlavour
            }
        }
    }

    dependencies {
        // Android Core
        add("implementation", libs.findLibrary("androidx.core.ktx").get())
        add("coreLibraryDesugaring", libs.findLibrary("android.desugarJdkLibs").get())

        add("implementation", libs.findLibrary("timber").get())

        add("testImplementation", libs.findLibrary("junit4").get())
        add("androidTestImplementation", libs.findLibrary("androidx.test.ext").get())
        add("androidTestImplementation", libs.findLibrary("androidx.test.espresso.core").get())
    }
}

fun CommonExtension<*, *, *, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}
