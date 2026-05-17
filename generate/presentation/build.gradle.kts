plugins {
    id("qrcraft.android.feature")
}

android {
    namespace = "com.lihan.qrcraft.generate.presentation"
}

dependencies {
    implementation(project(":generate:domain"))
    implementation(project(":generate:data"))
    
    implementation(libs.qrose)
}
