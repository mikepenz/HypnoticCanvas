import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("com.mikepenz.convention.kotlin-multiplatform")
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

    android {
        namespace = "com.mikepenz.hypnoticcanvas.sample"
    }

    listOf(
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
        commonMain.dependencies {
            implementation(baseLibs.jetbrains.compose.runtime)
            implementation(baseLibs.jetbrains.compose.foundation)
            implementation(baseLibs.jetbrains.compose.material3)
            implementation(baseLibs.jetbrains.compose.ui)
            implementation(baseLibs.jetbrains.compose.components.resources)

            implementation(projects.hypnoticcanvas)
            implementation(projects.hypnoticcanvasShaders)

            implementation(baseLibs.bundles.aboutlibs) // aboutlibraries

            implementation(libs.bundles.haze)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
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
    export {
        exportVariant = "jvmMain"
        outputPath = file("src/commonMain/composeResources/files/aboutlibraries.json")
    }
    library {
        duplicationMode = com.mikepenz.aboutlibraries.plugin.DuplicateMode.MERGE
    }
}
