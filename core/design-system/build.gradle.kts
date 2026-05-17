plugins {
    id("qrcraft.android.library")
    id("qrcraft.android.compose")
}

android {
    namespace = "com.lihan.qrcraft.core.design_system"
}

dependencies {
    implementation(libs.androidx.core.ktx)
}
