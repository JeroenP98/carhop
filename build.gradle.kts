val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

val exposed_version: String by project

plugins {
    kotlin("jvm") version "1.9.10"
    id("io.ktor.plugin") version "2.3.5"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10"
}

group = "com.carhop"
version = "0.0.1"

application {
    mainClass.set("com.carhop.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("io.ktor:ktor-server-core-jvm:2.3.6")
    implementation("io.ktor:ktor-server-auth-jvm:2.3.6")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:2.3.6")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.3.6")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.3.6")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.6")
    implementation("ch.qos.logback:logback-classic:1.4.12")
    testImplementation("io.ktor:ktor-server-tests-jvm:2.3.6")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation ("org.junit.jupiter:junit-jupiter:5.9.2")
    implementation("org.postgresql:postgresql:42.3.8")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    //thumbnailator for resizing images
    implementation ("net.coobird:thumbnailator:0.4.13")

    testImplementation("io.ktor:ktor-server-test-host-jvm:2.3.6")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.10")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

    testImplementation ("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation(kotlin("test"))
}
tasks.test {
    useJUnitPlatform()
}