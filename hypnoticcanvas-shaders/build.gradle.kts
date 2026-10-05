plugins {
    id("com.mikepenz.convention.kotlin-multiplatform")
    id("com.mikepenz.convention.compose")
    id("com.mikepenz.convention.publishing")
}

publishing {
    publications.withType<org.gradle.api.publish.maven.MavenPublication>().configureEach {
        pom.licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/license/mit")
                distribution.set("repo")
                comments.set("Applies to Prism Glass and Spectral Aurora; other shaders retain their respective licenses.")
            }
        }
    }
}

tasks.withType<org.gradle.api.tasks.bundling.AbstractArchiveTask>().configureEach {
    from("LICENSE-MIT") { into("META-INF/hypnoticcanvas-shaders") }
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
