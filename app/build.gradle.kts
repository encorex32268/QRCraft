plugins {
    id("qrcraft.android.application")
    id("qrcraft.android.compose")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.lihan.qrcraft"

    configurations.all {
        exclude(group = "com.intellij", module = "annotations")
    }

    defaultConfig {
        applicationId = "com.lihan.qrcraft"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:data"))
    implementation(project(":core:presentation"))
    implementation(project(":core:design-system"))

    implementation(project(":scan:presentation"))
    implementation(project(":generate:presentation"))
    implementation(project(":history:presentation"))

    implementation(libs.splashscreen.core)
    implementation(libs.navigation.compose)
    implementation(libs.bundles.koin)
    
    coreLibraryDesugaring(libs.desugar.jdk)
}