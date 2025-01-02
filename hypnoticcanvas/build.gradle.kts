plugins {
    id("com.mikepenz.convention.android-library")
    id("com.mikepenz.convention.kotlin-multiplatform")
    id("com.mikepenz.convention.compose")
    id("com.mikepenz.convention.publishing")
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.mikepenz.hypnoticcanvas"

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

        appleMain {
            dependsOn(nonAndroidMain)
        }

        jvmMain {
            dependsOn(nonAndroidMain)
        }

        named("wasmJsMain") {
            dependsOn(nonAndroidMain)
        }
        named("jsMain") {
            dependsOn(nonAndroidMain)
        }
    }
}

baselineProfile {
    filter { include("com.mikepenz.hypnoticcanvas.*") }
}
