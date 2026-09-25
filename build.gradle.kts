plugins {
    kotlin("jvm") version "2.2.20"
    id("io.qameta.allure") version "2.11.2"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
}
group = "com.quotes"
version = "1.0.0"

repositories {
    mavenCentral()
}
dependencies {
    // Kotlin & JUnit 5
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2") // Substituído de testEngine para testRuntimeOnly
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.2")
    testImplementation("org.assertj:assertj-core:3.25.3")
    testImplementation("org.slf4j:slf4j-simple:2.0.12")

    // REST Assured & JSON Schema Validation
    testImplementation("io.rest-assured:rest-assured:5.4.0")
    testImplementation("io.rest-assured:json-schema-validator:5.4.0")

    // WireMock (Servidor Mock HTTP em memória)
    testImplementation("org.wiremock:wiremock-standalone:3.13.2")

    // Jackson (Serialização JSON em Kotlin)
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")

    // Allure Reporting
    testImplementation("io.qameta.allure:allure-junit5:2.26.0")
    testImplementation("io.qameta.allure:allure-rest-assured:2.26.0")
}

tasks.test {
    useJUnitPlatform()

    systemProperty("allure.results.directory", "build/allure-results")
}

allure {
    adapter {
        frameworks {
            junit5 {
                adapterVersion.set("2.27.0")
            }
        }
    }
}
