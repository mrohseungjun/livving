plugins {
    id("livving.kotlin.multiplatform.library")
    id("livving.koin.core")
}

kotlin {
    sourceSets {
        jvmTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
