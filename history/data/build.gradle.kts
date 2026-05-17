plugins {
    id("qrcraft.android.library")
}

android {
    namespace = "com.lihan.qrcraft.history.data"
}

dependencies {
    implementation(project(":history:domain"))
    implementation(project(":core:data"))
}
