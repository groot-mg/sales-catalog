import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.4"
    id("io.spring.dependency-management") version "1.0.14.RELEASE"
    kotlin("jvm") version "1.7.20"
    kotlin("plugin.spring") version "1.7.20"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.bootJar { enabled = false }

repositories {
    mavenCentral()
}

dependencies {
    val wiremockStandalone = "2.34.0"
    val cucumberVersion = "7.8.1"
    val googleGuavaVersion = "31.1-jre"

    testImplementation(project(":sales-catalog-app"))

    testImplementation("io.cucumber:cucumber-spring:${cucumberVersion}")
    testImplementation("io.cucumber:cucumber-java:${cucumberVersion}")
    testImplementation("io.cucumber:cucumber-junit:${cucumberVersion}")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.google.guava:guava:${googleGuavaVersion}")
    testImplementation("com.github.tomakehurst:wiremock-jre8-standalone:${wiremockStandalone}")
}

configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
    imports(delegateClosureOf<io.spring.gradle.dependencymanagement.dsl.ImportsHandler> {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2021.0.4")
    })
}

val cucumberRuntime: Configuration by configurations.creating {
    extendsFrom(configurations["testImplementation"])
}

task("cucumber") {
    val profile = findProperty("spring.profiles.active") ?: "local"
    dependsOn("processTestResources", "compileTestJava")

    doLast {
        javaexec {
            systemProperties = mapOf("spring.profiles.active" to profile)
            main = "io.cucumber.core.cli.Main"
            classpath = cucumberRuntime + sourceSets.main.get().output + sourceSets.test.get().output
            args = listOf(
                "--tags", "not @ignore",
                "--plugin", "pretty",
                "--plugin", "html:reports/report.html",
                "--glue", "com.generoso.ft.salescatalog",
                "classpath:features"
            )
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

