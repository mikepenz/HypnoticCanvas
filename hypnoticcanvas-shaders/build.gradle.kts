plugins {
    id("com.mikepenz.convention.android-library")
    id("com.mikepenz.convention.kotlin-multiplatform")
    id("com.mikepenz.convention.compose")
    id("com.mikepenz.convention.publishing")

    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.mikepenz.hypnoticcanvas.shaders"

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.hypnoticcanvas)
            }
        }

        androidMain {
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

baselineProfile {
    filter { include("com.mikepenz.hypnoticcanvas.*") }
}
