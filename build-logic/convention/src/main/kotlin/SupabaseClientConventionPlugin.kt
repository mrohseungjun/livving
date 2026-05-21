import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class SupabaseClientConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("livving.ktor.client")
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            val kotlin = extensions.getByType<KotlinMultiplatformExtension>()

            kotlin.sourceSets.getByName("commonMain").dependencies {
                implementation(libs.findLibrary("supabase-auth").get())
                implementation(libs.findLibrary("supabase-postgrest").get())
                implementation(libs.findLibrary("supabase-storage").get())
            }
        }
    }
}
