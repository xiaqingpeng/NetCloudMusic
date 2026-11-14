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
        // 优先使用 Maven Central，KMP 原生 KLIB 在镜像源上可能无法完成 TLS 握手
        mavenCentral()
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