plugins {
    id("livving.compose.multiplatform.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.ui)
            implementation(projects.domain.greeting)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
