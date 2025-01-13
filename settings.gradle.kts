pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        google() // For Android and Jetpack libraries
        mavenCentral() // For Kotlin and third-party libraries
    }
}


rootProject.name = "RecommendoR"
include(":app")
