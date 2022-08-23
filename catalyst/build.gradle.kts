import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.5.0"
    id("com.github.johnrengelman.shadow")
    id("java")
}

group = "jp.ac.tsukuba.cs.iplab"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.1")

    // MongoDB
    implementation("org.litote.kmongo:kmongo-coroutine-serialization:4.5.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

tasks {
    shadowJar {
        manifest {
        }
    }
}