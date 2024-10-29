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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        //TODO remove this and "aws-sdk-kotlin-preview" folder once place and routes sdk is live
        maven {
            url = file("${rootDir}/aws-sdk-kotlin-preview/m2").toURI()
        }
    }
}

rootProject.name = "AndroidQuickStartApp"
include(":app")
 