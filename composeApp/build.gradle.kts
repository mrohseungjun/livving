plugins {
    id("livving.compose.multiplatform.application")
    id("livving.koin.compose")
    id("livving.kakao.android")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.ui)
            implementation(projects.data.network)
            implementation(projects.domain.livving)
            implementation(projects.feature.main)
        }
    }
}
