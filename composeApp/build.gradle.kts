plugins {
    id("livving.compose.multiplatform.application")
    id("livving.firebase.messaging")
    id("livving.koin.compose")
    id("livving.kakao.android")
    id("livving.google.services")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.platform)
            implementation(projects.core.ui)
            implementation(projects.data.network)
            implementation(projects.data.supabase)
            implementation(projects.domain.livving)
            implementation(projects.feature.main)
        }
    }
}
