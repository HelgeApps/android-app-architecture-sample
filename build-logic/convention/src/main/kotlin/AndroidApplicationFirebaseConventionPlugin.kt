import com.arch.example.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationFirebaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.gms.google-services")
                apply("com.google.firebase.crashlytics")
            }

            dependencies {
                val bom = libs.findLibrary("firebase-bom").get()
                add("implementation", platform(bom))
                "implementation"(libs.findLibrary("firebase.crashlytics").get())
            }

            /*extensions.configure<ApplicationAndroidComponentsExtension> {
                finalizeDsl {
                    it.buildTypes.forEach { buildType ->
                        // Disable the Crashlytics mapping file upload. This feature should only be
                        // enabled if a Firebase backend is available and configured in
                        // google-services.json.
                        buildType.configure<CrashlyticsExtension> {
                            mappingFileUploadEnabled = true
                        }
                    }
                }
            }*/
        }
    }
}
