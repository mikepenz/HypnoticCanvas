pluginManagement {
    includeBuild("gradle/build-logic")

    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        mavenLocal()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
// https://docs.gradle.org/7.6/userguide/configuration_cache.html#config_cache:stable
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

rootProject.name = "hypnoticcanvas-root"

include(
    ":hypnoticcanvas-shaders",
    ":hypnoticcanvas",
    ":sample",
)
