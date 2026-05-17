plugins {
    id("qrcraft.android.library")
}

android {
    namespace = "com.lihan.qrcraft.scan.domain"
}

dependencies {
    implementation(project(":core:domain"))
}
