import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")

            extensions.configure<LibraryExtension> {
                namespace = "kr.osj.livving" + path
                    .split(":")
                    .filter { it.isNotBlank() }
                    .joinToString(separator = "", prefix = ".") { it.replace("-", "") }
                compileSdk = versionInt("android-compileSdk")

                defaultConfig {
                    minSdk = versionInt("android-minSdk")
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }
            }
        }
    }
}
