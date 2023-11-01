import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("jvm")
    kotlin("plugin.spring")
}

extra["springCloudVersion"] = "2022.0.4"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.bootJar { enabled = false }

repositories {
    mavenCentral()
}

dependencies {
    val wiremockStandalone = "3.0.1"
    val cucumberVersion = "7.14.0"
    val googleGuavaVersion = "32.1.3-jre"
    val jose4jVersion = "0.9.3"
    val jacksonJsr310Version = "2.12.3"
    val embeddedPostgresVersion = "2.0.4"

    testImplementation(project(":sales-catalog-app"))

    testImplementation("io.cucumber:cucumber-spring:${cucumberVersion}")
    testImplementation("io.cucumber:cucumber-java:${cucumberVersion}")
    testImplementation("io.cucumber:cucumber-junit:${cucumberVersion}")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.bitbucket.b_c:jose4j:${jose4jVersion}")
    testImplementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${jacksonJsr310Version}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.google.guava:guava:${googleGuavaVersion}")
    testImplementation("com.github.tomakehurst:wiremock-jre8-standalone:${wiremockStandalone}")
    testImplementation("io.zonky.test:embedded-postgres:${embeddedPostgresVersion}")
}

configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
    imports(delegateClosureOf<io.spring.gradle.dependencymanagement.dsl.ImportsHandler> {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    })
}

val cucumberRuntime: Configuration by configurations.creating {
    extendsFrom(configurations["testImplementation"])
}

task("cucumber") {
    val profile = findProperty("spring.profiles.active") ?: "local-ft"
    dependsOn("processTestResources", "compileTestJava")

    doLast {
        javaexec {
            systemProperties = mapOf("spring.profiles.active" to profile)
            mainClass.set("io.cucumber.core.cli.Main")
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

