pluginManagement {
    repositories {
        google()
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
            url = file("${rootDir}/aws-sdk-kotlin-preview/m2").toURI()
        }
    }
}

rootProject.name = "Amazon Location Sample App"
include(":app")