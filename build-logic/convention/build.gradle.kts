import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "kr.osj.livving.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

repositories {
    google {
        mavenContent {
            includeGroupAndSubgroups("androidx")
            includeGroupAndSubgroups("com.android")
            includeGroupAndSubgroups("com.google")
        }
    }
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.kotlin.composeCompiler.gradlePlugin)
    implementation(libs.kotlin.serialization.gradlePlugin)
    implementation(libs.compose.gradlePlugin)
    implementation(libs.ksp.gradlePlugin)
    implementation(libs.google.services.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "livving.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "livving.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("kotlinMultiplatformAndroid") {
            id = "livving.kotlin.multiplatform.android"
            implementationClass = "KotlinMultiplatformAndroidConventionPlugin"
        }
        register("kotlinMultiplatformJvm") {
            id = "livving.kotlin.multiplatform.jvm"
            implementationClass = "KotlinMultiplatformJvmConventionPlugin"
        }
        register("kotlinMultiplatformIos") {
            id = "livving.kotlin.multiplatform.ios"
            implementationClass = "KotlinMultiplatformIosConventionPlugin"
        }
        register("kotlinMultiplatformLibrary") {
            id = "livving.kotlin.multiplatform.library"
            implementationClass = "KotlinMultiplatformLibraryConventionPlugin"
        }
        register("composeMultiplatform") {
            id = "livving.compose.multiplatform"
            implementationClass = "ComposeMultiplatformConventionPlugin"
        }
        register("composeMultiplatformApplication") {
            id = "livving.compose.multiplatform.application"
            implementationClass = "ComposeMultiplatformApplicationConventionPlugin"
        }
        register("composeMultiplatformLibrary") {
            id = "livving.compose.multiplatform.library"
            implementationClass = "ComposeMultiplatformLibraryConventionPlugin"
        }
        register("koinCore") {
            id = "livving.koin.core"
            implementationClass = "KoinCoreConventionPlugin"
        }
        register("koinCompose") {
            id = "livving.koin.compose"
            implementationClass = "KoinComposeConventionPlugin"
        }
        register("ktorClient") {
            id = "livving.ktor.client"
            implementationClass = "KtorClientConventionPlugin"
        }
        register("ktorfitClient") {
            id = "livving.ktorfit.client"
            implementationClass = "KtorfitClientConventionPlugin"
        }
        register("supabaseClient") {
            id = "livving.supabase.client"
            implementationClass = "SupabaseClientConventionPlugin"
        }
        register("coroutines") {
            id = "livving.coroutines"
            implementationClass = "CoroutinesConventionPlugin"
        }
        register("navigation3") {
            id = "livving.navigation3"
            implementationClass = "Navigation3ConventionPlugin"
        }
        register("kakaoAndroid") {
            id = "livving.kakao.android"
            implementationClass = "KakaoAndroidConventionPlugin"
        }
        register("firebaseMessaging") {
            id = "livving.firebase.messaging"
            implementationClass = "FirebaseMessagingConventionPlugin"
        }
        register("googleServices") {
            id = "livving.google.services"
            implementationClass = "GoogleServicesConventionPlugin"
        }
    }
}
