import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class ComposeMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.compose")
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
                val kotlin = extensions.getByType<KotlinMultiplatformExtension>()

                kotlin.sourceSets.getByName("commonMain").dependencies {
                    implementation(libs.findLibrary("compose-runtime").get())
                    implementation(libs.findLibrary("compose-foundation").get())
                    implementation(libs.findLibrary("compose-material3").get())
                    implementation(libs.findLibrary("compose-ui").get())
                    implementation(libs.findLibrary("compose-uiToolingPreview").get())
                    implementation(libs.findLibrary("androidx-lifecycle-runtimeCompose").get())
                }

                kotlin.sourceSets.getByName("androidMain").dependencies {
                    implementation(libs.findLibrary("compose-uiToolingPreview").get())
                }

                dependencies {
                    add("debugImplementation", libs.findLibrary("compose-uiTooling").get())
                }
            }
        }
    }
}
