plugins {
    id("livving.kotlin.multiplatform.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.data.greeting)
        }
        jvmTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
