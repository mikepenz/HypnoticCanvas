plugins {
    id("com.mikepenz.android.library")
    id("com.mikepenz.kotlin.multiplatform")
    id("com.mikepenz.compose")
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.dokka)
    alias(libs.plugins.mavenpublish)
}

android {
    namespace = "com.mikepenz.hypnoticcanvas"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(compose.ui)
                implementation(compose.foundation)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.collection)
                implementation(libs.androidx.core)
            }
        }

        val nonAndroidMain by creating {
            dependsOn(commonMain.get())
        }

        iosMain {
            dependsOn(nonAndroidMain)
        }

        jvmMain {
            dependsOn(nonAndroidMain)
        }

        named("wasmJsMain") {
            dependsOn(nonAndroidMain)
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}

baselineProfile {
    filter { include("com.mikepenz.hypnoticcanvas.*") }
}

dependencies {
}
