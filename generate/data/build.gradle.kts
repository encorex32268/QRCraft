plugins {
    id("qrcraft.android.library")
}

android {
    namespace = "com.lihan.qrcraft.generate.data"
}

dependencies {
    implementation(project(":generate:domain"))
    implementation(project(":core:data"))
}
