import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("com.mikepenz.convention.kotlin-multiplatform")
    id("com.mikepenz.convention.android-application")
    id("com.mikepenz.convention.compose")
    alias(baseLibs.plugins.aboutLibraries)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName = "composeApp"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }

    androidTarget {
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }

        commonMain.dependencies {
            implementation(compose.runtime) { require(true) }
            implementation(compose.foundation) { require(true) }
            implementation(compose.material3) { require(true) }
            implementation(compose.ui) { require(true) }
            implementation(compose.components.resources) { require(true) }
            implementation(compose.materialIconsExtended)

            implementation(projects.hypnoticcanvas)
            implementation(projects.hypnoticcanvasShaders)

            implementation(baseLibs.bundles.aboutlibs) // aboutlibraries

            implementation(libs.haze.core.get().toString()) {
                exclude(group = "org.jetbrains.kotlin")
            }
            implementation(libs.haze.materials.get().toString()) {
                exclude(group = "org.jetbrains.kotlin")
            }
        }

        val nonAndroidMain by creating {
            dependsOn(commonMain.get())
        }

        val desktopMain by getting {
            dependsOn(nonAndroidMain)

            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }

        nativeMain {
            dependsOn(nonAndroidMain)
        }

        val wasmJsMain by getting {
            dependsOn(nonAndroidMain)
        }
    }
}

android {
    namespace = "com.mikepenz.hypnoticcanvas"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.mikepenz.hypnoticcanvas"
        setProperty("archivesBaseName", "HypnoticCanvas-v$versionName")
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        buildTypes.release.proguard {
            isEnabled = true
            optimize = true
            obfuscate = true
            configurationFiles.from(project.file("compose-desktop.pro"))
        }

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "HypnoticCanvas"
            packageVersion = "1.0.0"
            description = "A shader modifier for Compose Multiplatform / Jetpack Compose"
            copyright = "© 2024 Mike Penz. All rights reserved."
        }
    }
}

aboutLibraries {
    android {
        registerAndroidTasks = false
    }
    library {
        duplicationMode = com.mikepenz.aboutlibraries.plugin.DuplicateMode.MERGE
    }
}