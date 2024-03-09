plugins {
    id("com.mikepenz.root")

    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.baselineprofile) apply false
    alias(libs.plugins.cacheFixPlugin) apply false
    alias(libs.plugins.android.lint) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.mavenpublish) apply false
    alias(libs.plugins.dokka)
}
