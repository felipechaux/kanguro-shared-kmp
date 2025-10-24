import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    kotlin("multiplatform")
    kotlin("plugin.compose")
    id("org.jetbrains.compose") version "1.7.0"
    id("com.android.library")
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.skie)
    alias(libs.plugins.moko.multiplatform.resources)
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("maven-publish")
}

// Version management
val sharedVersion = project.findProperty("kanguro.shared.version")?.toString() 
    ?: System.getenv("SHARED_VERSION") 
    ?: "1.0.0-SNAPSHOT"

version = sharedVersion
group = "com.insurtech.kanguro"

kotlin {
    androidTarget {
        publishLibraryVariants("release", "debug")
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    
    val xcf = XCFramework("KanguroShared")
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "KanguroShared"
            binaryOption("bundleId", "com.insurtech.kanguro.shared")
            binaryOption("bundleVersion", sharedVersion)
            binaryOption("bundleShortVersionString", sharedVersion)
            freeCompilerArgs += listOf("-Xoverride-konan-properties=minVersion.ios=16.0;minVersionSinceXcode15.ios=16.0")
            xcf.add(this)
            isStatic = true
            export("dev.icerock.moko:resources:0.24.5")
            export("dev.icerock.moko:graphics:0.9.0")
        }
    }
    
    sourceSets {
        androidMain {
            kotlin.srcDir("build/generated/moko/androidMain/src")
            dependencies {
                implementation(libs.ktor.client.okhttp)
                implementation(compose.preview)
                implementation(compose.components.uiToolingPreview)
                api(libs.koin.android)
                api(libs.koin.androidx.compose)
                // Posthog
                implementation(libs.posthog.android)
            }
        }
        commonMain {
            dependencies {
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.ui)
                implementation(compose.components.uiToolingPreview)
                implementation(compose.material3)
                implementation(libs.material.icons.core)
                implementation(compose.runtime)
                implementation(compose.components.resources)
                implementation(libs.compose.view.model)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.contentnegotiation)
                implementation(libs.ktor.client.serialization.json)
                api(libs.moko.resources)
                api(libs.moko.resources.compose) // for compose multiplatform
                api(libs.datastore.preferences)
                api(libs.datastore)

                api(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.lifecycle.viewmodel)
                api(libs.compose.webview.multiplatform)
                implementation(libs.coil3.compose)
                implementation(libs.coil3.network.ktor)
                implementation(libs.androidx.navigation.compose)
                implementation(libs.kotlinx.datetime)
                implementation(libs.calf.file.picker)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        iosMain {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
    }
}

android {
    namespace = "com.insurtech.kanguro.shared"
    compileSdk = 35
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = 24
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

multiplatformResources {
    resourcesPackage.set("com.insurtech.kanguro.shared.sharingresources")
}

skie {
    features {
        enableSwiftUIObservingPreview = true
    }
}

// Publishing configuration
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["kotlin"])
            
            groupId = "com.insurtech.kanguro"
            artifactId = "shared-kmp"
            version = sharedVersion
            
            pom {
                name.set("Kanguro Shared KMP")
                description.set("Kotlin Multiplatform shared module for Kanguro iOS and Android")
                url.set("https://github.com/kanguroseguro/kanguro-shared-kmp")
                
                licenses {
                    license {
                        name.set("Proprietary")
                        url.set("https://github.com/kanguroseguro/kanguro-shared-kmp/blob/main/LICENSE")
                    }
                }
                
                developers {
                    developer {
                        id.set("kanguro-team")
                        name.set("Kanguro Development Team")
                        email.set("dev@kanguroseguro.com")
                    }
                }
                
                scm {
                    connection.set("scm:git:git://github.com/kanguroseguro/kanguro-shared-kmp.git")
                    developerConnection.set("scm:git:ssh://github.com:kanguroseguro/kanguro-shared-kmp.git")
                    url.set("https://github.com/kanguroseguro/kanguro-shared-kmp/tree/main")
                }
            }
        }
    }
    
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/kanguroseguro/kanguro-shared-kmp")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
