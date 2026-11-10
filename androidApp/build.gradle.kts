import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
    }
}
dependencies {
    implementation(project(":shared"))

    implementation(libs.androidx.activity.compose)

    implementation(libs.compose.uiToolingPreview)
    debugImplementation(libs.compose.uiTooling)
}

android {
    namespace = "com.example.netcloudmusic"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.quick.app"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        val localProps = Properties().also { props ->
            val f = rootProject.file("local.properties")
            if (f.exists()) props.load(f.inputStream())
        }
        val githubClientId = localProps.getProperty("github.clientId")
            ?: System.getenv("GITHUB_CLIENT_ID")
            ?: ""
        val githubClientSecret = localProps.getProperty("github.clientSecret")
            ?: System.getenv("GITHUB_CLIENT_SECRET")
            ?: ""
        buildConfigField("String", "GITHUB_CLIENT_ID", "\"$githubClientId\"")
        buildConfigField("String", "GITHUB_CLIENT_SECRET", "\"$githubClientSecret\"")

        manifestPlaceholders["githubCallbackScheme"] = "netcloudmusic"
        manifestPlaceholders["githubCallbackHost"] = "github-callback"
    }
    flavorDimensions += listOf("env", "channel")
    productFlavors {
        create("dev") {
            dimension = "env"
            buildConfigField("String", "ENDPOINT", "\"https://quick-server-sp.ixuea.com/\"")
            buildConfigField(
                "String",
                "RESOURCE_ENDPOINT",
                "\"https://rs.ixuea.com/quick/%s\""
            )
        }
        create("local") {
            dimension = "env"
            buildConfigField("String", "ENDPOINT", "\"http://192.168.2.100:8080/\"")
            buildConfigField(
                "String",
                "RESOURCE_ENDPOINT",
                "\"https://rs.ixuea.com/quick/%s\""
            )
        }
        create("default") {
            dimension = "channel"
            versionNameSuffix = ".default"
        }
        create("play") {
            dimension = "channel"
            versionNameSuffix = "-play"
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}
