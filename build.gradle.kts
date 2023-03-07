plugins {
    id("org.springframework.boot") version "2.5.3"
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.spring") version "1.7.10"
    kotlin("plugin.jpa") version "1.7.10"
    id("com.bmuschko.docker-spring-boot-application") version "7.1.0"
//    id("com.gorylenko.gradle-git-properties") version "2.3.1"
    id("org.barfuin.gradle.taskinfo") version "1.3.0"
}

kotlin {
    jvmToolchain{
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
    google()
}

tasks {
    wrapper{
        gradleVersion = "7.5"
    }

    test {
        useJUnitPlatform()
        environment["PATH_TO_FOLDER"] = buildDir.resolve("perf")
    }

}

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:2.6.4"))
    implementation(platform("org.springframework.cloud:spring-cloud-dependencies:2021.0.1"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("org.springframework.boot:spring-boot-starter-actuator")


    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springdoc:springdoc-openapi-ui:1.5.10")

    implementation("com.h2database:h2")
    implementation("org.testcontainers:testcontainers:1.16.0")
    implementation("org.testcontainers:postgresql:1.16.0")
    implementation("org.postgresql:postgresql")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
//    implementation("org.liquibase:liquibase-core:4.4.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("io.github.microutils:kotlin-logging-jvm:2.1.23")
}
