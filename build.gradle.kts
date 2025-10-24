plugins {
    // Apply the same plugins as needed for dependency management
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.multiplatform) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
}

allprojects {
    group = "com.insurtech.kanguro"
    version = project.findProperty("kanguro.shared.version") ?: "1.0.0"
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}