plugins {
    id("qrcraft.android.feature")
}

android {
    namespace = "com.lihan.qrcraft.scan.presentation"
}

dependencies {
    implementation(project(":scan:domain"))
    implementation(project(":scan:data"))
    
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.barcode.scanning)
    
    implementation(libs.google.accompanist.permissions)
    implementation(libs.kotlinx.coroutines.guava)
    implementation(libs.kotlinx.coroutines.play.services)
}
