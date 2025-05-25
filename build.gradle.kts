plugins {
    alias(baseLibs.plugins.conventionPlugin)

    alias(baseLibs.plugins.androidApplication) apply false
    alias(baseLibs.plugins.androidLibrary) apply false
    alias(baseLibs.plugins.androidLint) apply false
    alias(baseLibs.plugins.androidTest) apply false
    alias(baseLibs.plugins.kotlinMultiplatform) apply false
    alias(baseLibs.plugins.composeMultiplatform) apply false
    alias(baseLibs.plugins.composeCompiler) apply false
    alias(baseLibs.plugins.composeHotreload) apply false
    alias(baseLibs.plugins.mavenPublish) apply false
    alias(baseLibs.plugins.versionCatalogUpdate) apply false
    alias(baseLibs.plugins.aboutLibraries) apply false
    alias(baseLibs.plugins.dokka)
    alias(libs.plugins.baselineprofile) apply false
    alias(libs.plugins.cacheFixPlugin) apply false
}
