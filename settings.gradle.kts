pluginManagement {
    repositories {
        // google() 在境内被墙，改用 dl.google.com 镜像（内容一致）
        maven { url = uri("https://dl.google.com/android/maven2") }
        gradlePluginPortal()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url = uri("https://dl.google.com/android/maven2") }
        mavenCentral()
    }
}
rootProject.name = "createrole"
include(":app")
