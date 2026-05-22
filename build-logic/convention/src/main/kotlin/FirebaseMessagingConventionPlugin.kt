import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class FirebaseMessagingConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val kotlin = extensions.getByType<KotlinMultiplatformExtension>()

            kotlin.sourceSets.getByName("androidMain").dependencies {
                implementation(project.dependencies.platform(libs.findLibrary("firebase-bom").get()))
                implementation(libs.findLibrary("firebase-messaging").get())
            }
        }
    }
}
