import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

class ComposeMultiplatformApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("livving.android.application")
                apply("livving.kotlin.multiplatform.android")
                apply("livving.kotlin.multiplatform.ios")
                apply("livving.compose.multiplatform")
            }

            val kotlin = extensions.getByType<KotlinMultiplatformExtension>()

            kotlin.targets.withType(KotlinNativeTarget::class.java).configureEach {
                binaries.withType(Framework::class.java).configureEach {
                    baseName = "ComposeApp"
                    binaryOption("bundleId", "kr.osj.livving")
                    isStatic = true
                }
            }

            kotlin.sourceSets.getByName("androidMain").dependencies {
                implementation(libs.findLibrary("androidx-activity-compose").get())
            }

            kotlin.sourceSets.getByName("commonMain").dependencies {
                implementation(libs.findLibrary("compose-components-resources").get())
                implementation(libs.findLibrary("androidx-lifecycle-viewmodelCompose").get())
            }

            kotlin.sourceSets.getByName("commonTest").dependencies {
                implementation(libs.findLibrary("kotlin-test").get())
            }
        }
    }
}
