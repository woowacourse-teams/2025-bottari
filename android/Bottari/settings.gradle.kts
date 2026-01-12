pluginManagement {
    includeBuild("build-logic")

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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "Bottari"
include(":app")

include(":logger")
include(":core:data")
include(":core:network")
include(":core:local")
include(":core:domain")
include(":core:designsystem")
include(":core:ui")
include(":core:navigation")

include(":presentation")
include(":feature:mybottari")
include(":feature:template")
include(":feature:more")
