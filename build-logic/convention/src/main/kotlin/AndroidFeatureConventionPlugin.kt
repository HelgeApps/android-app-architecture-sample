import com.android.build.gradle.LibraryExtension
import com.arch.example.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "my.android.library")
            apply(plugin = "my.android.hilt")
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

            extensions.configure<LibraryExtension> {
                defaultConfig {
                    testInstrumentationRunner =
                        "com.arch.example.testing.HiltTestRunner"
                }
                testOptions.animationsDisabled = true
            }

            dependencies {
                "implementation"(project(":ui"))
                "implementation"(project(":designsystem"))

                "implementation"(libs.findLibrary("androidx.hilt.navigation.compose").get())
                "implementation"(libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                "implementation"(libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
                "implementation"(libs.findLibrary("androidx.navigation.compose").get())
                "implementation"(libs.findLibrary("androidx.tracing.ktx").get())

                "testImplementation"(libs.findLibrary("androidx.navigation.testing").get())
                "androidTestImplementation"(libs.findLibrary("androidx.lifecycle.runtimeTesting").get())
            }
        }
    }
}
