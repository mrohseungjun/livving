import org.gradle.api.Plugin
import org.gradle.api.Project

class GoogleServicesConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.pluginManager.apply("com.google.gms.google-services")
    }
}
