plugins {
    id("qrcraft.android.library")
}

android {
    namespace = "com.lihan.qrcraft.scan.data"
}

dependencies {
    implementation(project(":scan:domain"))
    implementation(project(":core:data"))
}
