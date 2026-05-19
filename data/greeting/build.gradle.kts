plugins {
    id("livving.kotlin.multiplatform.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.platform)
        }
    }
}
