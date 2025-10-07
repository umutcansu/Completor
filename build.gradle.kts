plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("org.jetbrains.intellij.platform") version "2.7.1"
}

group = "io.github.umutcansu.completor"
version = "1.0-SNAPSHOT"

sourceSets {
    main {
        java.srcDirs("src/main/java")
    }
}


repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        androidStudio("2024.1.2.1")
        //create("IC", "2024.1")
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)

        implementation("com.jayway.jsonpath:json-path:2.8.0")

    }
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild.set("241")

            untilBuild.set("241.*")
        }

        changeNotes.set("""
            Initial version
        """.trimIndent())
    }
    publishing {
        token.set(providers.gradleProperty("ORG_JETBRAINS_INTELLIJ_PLATFORM_PUBLISH_TOKEN"))
    }
}





tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

