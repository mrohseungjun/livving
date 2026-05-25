import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class TimberLoggingConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val kotlin = extensions.getByType<KotlinMultiplatformExtension>()

            kotlin.sourceSets.getByName("androidMain").dependencies {
                implementation(libs.findLibrary("timber").get())
            }
        }
    }
}
