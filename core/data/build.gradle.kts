plugins {
    id("qrcraft.android.library")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.lihan.qrcraft.core.data"
}

dependencies {
    implementation(project(":core:domain"))

    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

    implementation(libs.koin.android)
}
