import com.android.build.api.dsl.ApplicationExtension
import com.arch.example.AndroidSdkVersions
import com.arch.example.configureAndroidCompose
import com.arch.example.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.compose")
            }
            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = AndroidSdkVersions.TARGET
                configureAndroidCompose(this)
            }
        }
    }
}
