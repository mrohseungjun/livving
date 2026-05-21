import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KtorfitClientConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("livving.ktor.client")
            pluginManager.apply("com.google.devtools.ksp")

            val kotlin = extensions.getByType<KotlinMultiplatformExtension>()
            kotlin.sourceSets.getByName("commonMain").dependencies {
                implementation(libs.findLibrary("ktorfit-lib-light").get())
            }
            kotlin.sourceSets.getByName("commonMain").kotlin.srcDir(
                layout.buildDirectory.dir("generated/ksp/metadata/commonMain/kotlin"),
            )

            dependencies.add("kspCommonMainMetadata", libs.findLibrary("ktorfit-ksp").get())

            tasks.matching { task ->
                task.name.startsWith("compile") && task.name.contains("Kotlin")
            }.configureEach {
                dependsOn("kspCommonMainKotlinMetadata")
            }
        }
    }
}
