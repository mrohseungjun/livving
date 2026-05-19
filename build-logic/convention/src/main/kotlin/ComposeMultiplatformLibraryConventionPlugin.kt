import org.gradle.api.Plugin
import org.gradle.api.Project

class ComposeMultiplatformLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target.pluginManager) {
            apply("livving.kotlin.multiplatform.library")
            apply("livving.compose.multiplatform")
        }
    }
}
