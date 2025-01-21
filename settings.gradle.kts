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
        maven { url = uri("https://jitpack.io") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "DSLR Blur"
include(":app")
include(":common:values")
include(":common:analytics")
include(":common:framework")
include(":common:components")
include(":start-up")
include(":gallery:data")
include(":gallery:domain")
include(":gallery:presentation")
include(":feature:background")
include(":feature:background-blur")
