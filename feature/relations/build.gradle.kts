plugins {
    id("livving.compose.multiplatform.library")
    id("livving.koin.compose")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.ui)
            implementation(projects.domain.livving)
        }
    }
}
