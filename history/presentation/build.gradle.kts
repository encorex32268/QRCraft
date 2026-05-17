plugins {
    id("qrcraft.android.feature")
}

android {
    namespace = "com.lihan.qrcraft.history.presentation"
}

dependencies {
    implementation(project(":history:domain"))
    implementation(project(":history:data"))
}
