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
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "QRCraft"
include(":app")
include(":core:domain")
include(":core:data")
include(":core:presentation")
include(":core:design-system")

include(":scan:domain")
include(":scan:data")
include(":scan:presentation")

include(":generate:domain")
include(":generate:data")
include(":generate:presentation")

include(":history:domain")
include(":history:data")
include(":history:presentation")


