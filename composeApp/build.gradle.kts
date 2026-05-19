plugins {
    id("livving.compose.multiplatform.application")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.ui)
            implementation(projects.feature.greeting)
        }
    }
}
