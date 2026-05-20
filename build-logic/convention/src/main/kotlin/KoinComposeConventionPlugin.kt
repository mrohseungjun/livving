import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KoinComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("livving.koin.core")

            val kotlin = extensions.getByType<KotlinMultiplatformExtension>()
            kotlin.sourceSets.getByName("commonMain").dependencies {
                implementation(libs.findLibrary("koin-compose").get())
                implementation(libs.findLibrary("koin-core-viewmodel").get())
                implementation(libs.findLibrary("koin-compose-viewmodel").get())
            }
        }
    }
}
