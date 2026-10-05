@file:OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)

plugins {
    id("com.mikepenz.convention.kotlin-multiplatform")
    id("com.mikepenz.convention.compose")
    id("com.mikepenz.convention.publishing")
}

kotlin {
    android {
        namespace = "com.mikepenz.hypnoticcanvas"
    }

    applyDefaultHierarchyTemplate {
        common {
            group("nonAndroid") {
                withJvm()
                withJs()
                withWasmJs()
                withIos()
                withMacos()
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(baseLibs.jetbrains.compose.ui)
            implementation(baseLibs.jetbrains.compose.foundation)
        }
    }
}
