plugins {
    id("livving.compose.multiplatform.library")
    id("livving.koin.compose")
    id("livving.coroutines")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.ui)
            implementation(projects.domain.greeting)
        }
    }
}
