pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        mavenLocal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
        mavenLocal()
    }

    versionCatalogs {
        create("baseLibs") {
            from("com.mikepenz:version-catalog:0.0.1")
        }
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
