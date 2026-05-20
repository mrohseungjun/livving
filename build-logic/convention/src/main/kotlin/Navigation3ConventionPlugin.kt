import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class Navigation3ConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
                val kotlin = extensions.getByType<KotlinMultiplatformExtension>()

                kotlin.sourceSets.getByName("commonMain").dependencies {
                    implementation(libs.findLibrary("jetbrains-navigation3-ui").get())
                    implementation(libs.findLibrary("kotlinx-serialization-core").get())
                }
            }
        }
    }
}
