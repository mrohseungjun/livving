plugins {
    id("livving.compose.multiplatform.library")
    id("livving.koin.compose")
    id("livving.coroutines")
    id("livving.navigation3")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.platform)
            implementation(projects.core.ui)
            implementation(projects.domain.livving)
            implementation(projects.feature.auth)
            implementation(projects.feature.home)
            implementation(projects.feature.notifications)
            implementation(projects.feature.relations)
            implementation(projects.feature.splash)
            implementation(projects.feature.settings)
            implementation(projects.feature.setup)
        }
    }
}
