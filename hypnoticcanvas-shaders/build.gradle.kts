plugins {
    id("com.mikepenz.convention.kotlin-multiplatform")
    id("com.mikepenz.convention.compose")
    id("com.mikepenz.convention.publishing")
}

kotlin {
    android {
        namespace = "com.mikepenz.hypnoticcanvas.shaders"
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.hypnoticcanvas)
        }
        jvmTest.dependencies {
            implementation(kotlin("test"))
            implementation(compose.desktop.currentOs)
        }
    }
}

// Recording is deliberate; normal tests only verify the committed README art.
tasks.withType<Test>().configureEach {
    systemProperty("readme.art.directory", rootProject.file("art").absolutePath)
    systemProperty("readme.art.record", providers.gradleProperty("recordReadmeArt").getOrElse("false"))
}

tasks.register("recordReadmeArt") {
    group = "documentation"
    description = "Render README shader images with -PrecordReadmeArt=true --rerun-tasks."
    dependsOn("jvmTest")
}

tasks.register("verifyReadmeArt") {
    group = "verification"
    description = "Verify committed README images against the actual shaders."
    dependsOn("jvmTest")
}
