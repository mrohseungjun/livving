plugins {
    id("livving.kotlin.multiplatform.library")
    id("livving.ktorfit.client")
    id("livving.supabase.client")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.platform)
            implementation(projects.domain.livving)
        }
    }
}
