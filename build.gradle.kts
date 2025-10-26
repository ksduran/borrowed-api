import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktor)
    alias(libs.plugins.shadow.jar)
}

group = "com.kevinduran"
version = "0.0.1"

tasks.withType<ShadowJar> {
    archiveBaseName.set("borrowed-ktor-api")
    archiveVersion.set("")
    archiveClassifier.set("")
}

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

dependencies {

    implementation(libs.shadow.jar)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.dao)
    implementation(libs.mysql.connector.java)
    implementation(libs.serialization.kotlinx.json)
    implementation(libs.server.content.negotiation)
    implementation(libs.zaxxer.hikari.cp)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.config.yaml)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}

