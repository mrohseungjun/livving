rootProject.name = "Livving"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")

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
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        maven("https://devrepo.kakao.com/nexus/content/groups/public/")
        mavenCentral()
    }
}

include(":composeApp")
include(":core:platform")
include(":core:ui")
include(":data:network")
include(":data:supabase")
include(":domain:livving")
include(":feature:auth")
include(":feature:home")
include(":feature:main")
include(":feature:notifications")
include(":feature:relations")
include(":feature:splash")
include(":feature:settings")
include(":feature:setup")
