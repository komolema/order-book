import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.vertx:vertx-core:4.5.9")
    implementation(kotlin("stdlib"))
    implementation("io.vertx:vertx-lang-kotlin:4.5.9")
    implementation("io.vertx:vertx-lang-kotlin-coroutines:4.5.9")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.7.1")
}

tasks.test {
    useJUnitPlatform()
}