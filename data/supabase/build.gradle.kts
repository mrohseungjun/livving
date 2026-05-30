plugins {
    id("livving.kotlin.multiplatform.library")
    id("livving.supabase.client")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.platform)
            implementation(projects.data.network)
            implementation(projects.domain.livving)
        }
    }
}
