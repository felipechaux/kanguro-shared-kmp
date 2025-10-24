plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
}

allprojects {
    group = "com.insurtech.kanguro"
    version = project.findProperty("kanguro.shared.version") ?: "1.0.0"
}

tasks.register("clean", Delete::class) {
    delete(layout.buildDirectory.asFile.get())
}
