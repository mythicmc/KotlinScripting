import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    kotlin("jvm") version "2.0.10"
    kotlin("kapt") version "2.0.10"
    id("com.gradleup.shadow") version "8.3.0"
    id("net.kyori.blossom") version "2.1.0"
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.8" // IntelliJ + Blossom integration
    id("org.ajoberstar.grgit.service") version "5.2.0"
}

group = "org.mythicmc"
version = "1.0.0-alpha.0${getVersionMetadata()}"
description = "Write listener/command scripts in Kotlin and " +
        "load them into your Minecraft server/proxy at runtime!"

repositories {
    mavenCentral()
    maven(url = "https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.2.0-SNAPSHOT")
    kapt("com.velocitypowered:velocity-api:3.2.0-SNAPSHOT")
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

sourceSets {
    main {
        blossom {
            resources {
                property("version", project.version.toString())
                property("description", project.description)
            }
            javaSources {
                property("version", project.version.toString())
                property("description", project.description)
            }
        }
    }
}

tasks.getByName<ShadowJar>("shadowJar") {
    minimize()
    archiveClassifier.set("")
    // Due to necessities of Kotlin scripting libraries, we can't shadow Kotlin.
    // relocate("kotlin", "org.mythicmc.kotlinscripting.shadow.kotlin")
}

fun getVersionMetadata(): String {
    if (project.hasProperty("skipVersionMetadata")) return ""

    val grgit = try { grgitService.service.orNull?.grgit } catch (e: Exception) { null }
    if (grgit != null) {
        val head = grgit.head() ?: return "+unknown" // No head, fresh git repo
        var id = head.abbreviatedId
        val tag = grgit.tag.list().find { head.id == it.commit.id }

        // If we're on a tag and the tree is clean, don't put any metadata
        if (tag != null && grgit.status().isClean) {
            return ""
        }
        // Flag the build if the tree isn't clean
        if (!grgit.status().isClean) {
            id += "-dirty"
        }

        return "+git.$id"
    }

    return "+unknown"
}
