package com.arch.example

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

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

    configureKotlin<KotlinAndroidProjectExtension>()

    dependencies {
        // Android Core
        "implementation"(libs.findLibrary("androidx.core.ktx").get())
        "coreLibraryDesugaring"(libs.findLibrary("android.desugarJdkLibs").get())

        "implementation"(libs.findLibrary("timber").get())

        "testImplementation"(libs.findLibrary("junit4").get())
        "androidTestImplementation"(libs.findLibrary("androidx.test.ext").get())
        "androidTestImplementation"(libs.findLibrary("androidx.test.espresso.core").get())
    }
}

/**
 * Configure base Kotlin options
 */
private inline fun <reified T : KotlinBaseExtension> Project.configureKotlin() = configure<T> {
    // Treat all Kotlin warnings as errors (disabled by default)
    // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
    val warningsAsErrors = providers.gradleProperty("warningsAsErrors").map {
        it.toBoolean()
    }.orElse(false)
    when (this) {
        is KotlinAndroidProjectExtension -> compilerOptions
        is KotlinJvmProjectExtension -> compilerOptions
        else -> TODO("Unsupported project extension $this ${T::class}")
    }.apply {
        jvmTarget = JvmTarget.JVM_17
        allWarningsAsErrors = warningsAsErrors
        freeCompilerArgs.addAll(
            // Enable experimental coroutines APIs, including Flow
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
        freeCompilerArgs.add(
            /**
             * Remove this args after Phase 3.
             * https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-consistent-copy-visibility/#deprecation-timeline
             *
             * Deprecation timeline
             * Phase 3. (Supposedly Kotlin 2.2 or Kotlin 2.3).
             * The default changes.
             * Unless ExposedCopyVisibility is used, the generated 'copy' method has the same visibility as the primary constructor.
             * The binary signature changes. The error on the declaration is no longer reported.
             * '-Xconsistent-data-class-copy-visibility' compiler flag and ConsistentCopyVisibility annotation are now unnecessary.
             */
            "-Xconsistent-data-class-copy-visibility"
        )
    }
}
