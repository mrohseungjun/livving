plugins {
    id("livving.compose.multiplatform.application")
    id("livving.koin.compose")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.ui)
            implementation(projects.data.greeting)
            implementation(projects.feature.greeting)
        }
    }
}
