plugins {
    id("com.mikepenz.convention.android-application")
    id("com.mikepenz.convention.compose")
}

android {
    namespace = "com.mikepenz.hypnoticcanvas.sample.android"

    defaultConfig {
        applicationId = "com.mikepenz.hypnoticcanvas"
        base.archivesName = "HypnoticCanvas-v$versionName"
    }

    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}

dependencies {
    implementation(projects.sample)
    implementation(libs.androidx.activity.compose)
}
