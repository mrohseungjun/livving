import org.gradle.api.Plugin
import org.gradle.api.Project

class KotlinMultiplatformLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target.pluginManager) {
            apply("livving.android.library")
            apply("livving.kotlin.multiplatform.android")
            apply("livving.kotlin.multiplatform.jvm")
            apply("livving.kotlin.multiplatform.ios")
        }
    }
}
