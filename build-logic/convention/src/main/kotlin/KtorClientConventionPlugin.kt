import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KtorClientConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("livving.koin.core")

            val kotlin = extensions.getByType<KotlinMultiplatformExtension>()

            kotlin.sourceSets.getByName("commonMain").dependencies {
                implementation(libs.findLibrary("ktor-client-core").get())
                implementation(libs.findLibrary("ktor-client-contentNegotiation").get())
                implementation(libs.findLibrary("ktor-serialization-kotlinxJson").get())
                implementation(libs.findLibrary("kotlinx-serialization-json").get())
            }

            kotlin.sourceSets.getByName("androidMain").dependencies {
                implementation(libs.findLibrary("ktor-client-okhttp").get())
            }

            listOf("iosArm64Main", "iosSimulatorArm64Main").forEach { sourceSetName ->
                kotlin.sourceSets.getByName(sourceSetName).dependencies {
                    implementation(libs.findLibrary("ktor-client-darwin").get())
                }
            }

            kotlin.sourceSets.getByName("jvmMain").dependencies {
                implementation(libs.findLibrary("ktor-client-cio").get())
            }
        }
    }
}
