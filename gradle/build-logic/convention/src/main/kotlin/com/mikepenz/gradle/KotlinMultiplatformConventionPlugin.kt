package com.mikepenz.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinMultiplatformConventionPlugin : Plugin<Project> {
    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("org.jetbrains.kotlin.multiplatform")
        }

        extensions.configure<KotlinMultiplatformExtension> {
            applyDefaultHierarchyTemplate()

            jvm()
            if (pluginManager.hasPlugin("com.android.library")) {
                androidTarget {
                    publishLibraryVariants("release")
                }
            }

            iosX64()
            iosArm64()
            iosSimulatorArm64()

            wasmJs {
                browser()
            }

            configureKotlin()
        }
    }
}
