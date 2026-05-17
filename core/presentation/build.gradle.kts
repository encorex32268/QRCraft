plugins {
    id("qrcraft.android.library")
    id("qrcraft.android.compose")
}

android {
    namespace = "com.lihan.qrcraft.core.presentation"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:design-system"))

    implementation(libs.koin.androidx.compose)
    implementation(libs.qrose)
    implementation(libs.navigation.compose)
}
