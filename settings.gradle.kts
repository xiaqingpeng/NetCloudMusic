rootProject.name = "NetCloudMusic"


pluginManagement {
    repositories {
        // Gradle 插件镜像
        maven("https://maven.aliyun.com/repository/gradle-plugin")

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
        // Google 依赖镜像
        maven("https://maven.aliyun.com/repository/google")

        // Maven Central 镜像
        maven("https://maven.aliyun.com/repository/central")

        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }

        mavenCentral()
    }
}

include(":androidApp")
include(":shared")