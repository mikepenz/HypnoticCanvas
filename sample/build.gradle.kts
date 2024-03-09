import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import java.util.Properties

plugins {
    id("com.mikepenz.android.application")
    id("org.jetbrains.kotlin.multiplatform")
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.aboutlibraries)
}

if (appSigningFile != null) {
    apply(from = appSigningFile)
}

compose {
    kotlinCompilerPlugin.set(libs.versions.jetpackcompose.compiler.get())
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
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
        all {
            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
        }

        val nonAndroidMain by creating {
            dependsOn(commonMain.get())
        }

        androidMain.dependencies {
            //implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime) { require(true) }
            implementation(compose.foundation) { require(true) }
            implementation(compose.material3) { require(true) }
            implementation(compose.ui) { require(true) }
            implementation(compose.components.resources) { require(true) }

            implementation(projects.hypnoticcanvas)
            implementation(projects.hypnoticcanvasShaders)

            implementation(libs.bundles.coil) // image
            implementation(libs.bundles.aboutlibs) // aboutlibraries
            implementation(libs.bundles.haze) // haze
        }

        val desktopMain by getting {
            dependsOn(nonAndroidMain)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }

        nativeMain {
            dependsOn(nonAndroidMain)
        }

        val wasmJsMain by getting {
            dependsOn(nonAndroidMain)
        }

        wasmJsMain.dependencies {
            implementation(libs.ktor.js)
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
        versionCode = 1
        versionName = "0.0.1"
        setProperty("archivesBaseName", "HypnoticCanvas-v$versionName")
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true

            signingConfig = signingConfigs.findByName("release")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
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

compose.experimental {
    web.application {}
}

aboutLibraries {
    registerAndroidTasks = false
    duplicationMode = com.mikepenz.aboutlibraries.plugin.DuplicateMode.MERGE
}

private val appSigningFile: String?
    get() {
        val k = "signing.file"
        return Properties().also { prop ->
            rootProject.file("local.properties").takeIf { it.exists() }?.let {
                prop.load(it.inputStream())
            }
        }.getProperty(k, null) ?: if (project.hasProperty(k)) project.property(k)?.toString() else null
    }


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        if (project.findProperty("composeCompilerReports") == "true") {
            freeCompilerArgs += listOf(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${project.layout.buildDirectory.asFile.get().absolutePath}/compose_compiler"
            )
        }
        if (project.findProperty("composeCompilerMetrics") == "true") {
            freeCompilerArgs += listOf(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${project.layout.buildDirectory.asFile.get().absolutePath}/compose_compiler"
            )
        }
    }
}