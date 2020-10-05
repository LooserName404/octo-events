import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.config.KotlinCompilerVersion

val koin_version = "2.1.6"

plugins {
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
}
group = "dev.loosername"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib", KotlinCompilerVersion.VERSION))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0-RC2")
    implementation("io.javalin:javalin:3.11.0")
    implementation("org.slf4j:slf4j-simple:1.7.30")
    implementation("org.koin:koin-core:$koin_version")
    testImplementation(kotlin("test-junit"))
    testImplementation("org.koin:koin-test:$koin_version")
    testImplementation("io.mockk:mockk:1.10.2")
}

buildscript {
    repositories {
        jcenter()
        mavenCentral()
    }

    dependencies {
        val kotlinVersion = "1.4.10"

        classpath(kotlin("gradle-plugin", version = kotlinVersion))
        classpath(kotlin("serialization", version = kotlinVersion))
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}