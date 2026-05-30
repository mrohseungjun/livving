plugins {
    id("livving.kotlin.multiplatform.library")
}

kotlin {
    sourceSets {
        jvmTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}
