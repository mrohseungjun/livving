plugins {
    id("livving.compose.multiplatform.application")
    id("livving.koin.compose")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.ui)
            implementation(projects.domain.livving)
            implementation(projects.feature.main)
        }
    }
}
