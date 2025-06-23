import com.android.build.gradle.LibraryExtension
import com.arch.example.AndroidSdkVersions
import com.arch.example.configureKotlinAndroid
import com.arch.example.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }
            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                testOptions.animationsDisabled = true
                defaultConfig.targetSdk = AndroidSdkVersions.TARGET
                defaultConfig.consumerProguardFiles("consumer-rules.pro")
            }
            dependencies {
                "testImplementation"(kotlin("test"))

                "implementation"(libs.findLibrary("androidx.tracing.ktx").get())
            }
        }
    }
}
