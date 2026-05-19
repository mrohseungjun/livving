import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    id("livving.kotlin.multiplatform.library")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.findLibrary("compose-runtime").get())
            implementation(libs.findLibrary("compose-foundation").get())
            implementation(libs.findLibrary("compose-material3").get())
            implementation(libs.findLibrary("compose-ui").get())
            implementation(libs.findLibrary("compose-uiToolingPreview").get())
            implementation(libs.findLibrary("androidx-lifecycle-runtimeCompose").get())
        }
        androidMain.dependencies {
            implementation(libs.findLibrary("compose-uiToolingPreview").get())
        }
    }
}

dependencies {
    add("debugImplementation", libs.findLibrary("compose-uiTooling").get())
}
