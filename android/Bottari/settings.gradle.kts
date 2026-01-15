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
include(":core:common")

include(":feature:mybottari")
include(":feature:template")
include(":feature:template-detail")
include(":feature:template-create")
include(":feature:personal-checklist")
include(":feature:team-checklist")
include(":feature:personal-edit")
include(":feature:team-edit")
include(":feature:invite")
include(":feature:more")
include(":feature:main")
