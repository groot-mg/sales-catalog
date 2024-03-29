import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
    id("com.gorylenko.gradle-git-properties") version "2.4.1"
    id("pl.allegro.tech.build.axion-release") version "1.15.5"
    id("org.sonarqube") version "4.4.1.3373"
    id("jacoco")
    kotlin("jvm")
    kotlin("plugin.spring")
}

extra["springCloudVersion"] = "2022.0.4"
extra["springDocVersion"] = "2.2.0"
extra["jose4jVersion"] = "0.9.3"
extra["embeddedDatabaseSpringTestVersion"] = "2.3.0"
extra["embeddedPostgresVersion"] = "2.0.4"
extra["mockitoKotlinVersion"] = "5.1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

scmVersion {
    tag {
        prefix.set(rootProject.name + "-")
    }
    versionIncrementer("incrementMinor")
}
project.version = scmVersion.version

val commonJarConfigClosure: Action<Jar> = Action {
    archiveFileName.set("${project.name}.jar")
}

tasks.bootJar {
    commonJarConfigClosure.execute(this)
}

configure<org.springframework.boot.gradle.dsl.SpringBootExtension> {
    buildInfo()
}

repositories {
    mavenCentral()
    maven { url = uri("https://artifactory-oss.prod.netflix.net/artifactory/maven-oss-candidates") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.micrometer:micrometer-registry-prometheus")
    // Database - Postgres
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${property("springDocVersion")}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock")
    testImplementation("org.bitbucket.b_c:jose4j:${property("jose4jVersion")}")
    testImplementation("org.mockito.kotlin:mockito-kotlin:${property("mockitoKotlinVersion")}")

    // In memory database - PostgreSQL tests
    testImplementation("io.zonky.test:embedded-database-spring-test:${property("embeddedDatabaseSpringTestVersion")}")
    testImplementation("io.zonky.test:embedded-postgres:${property("embeddedPostgresVersion")}")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}
tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    reports {
        xml.required.set(true)
    }
}
tasks.sonarqube {
    dependsOn(tasks.jacocoTestReport)
}

sonarqube {
    properties {
        property("sonar.projectKey", "groot-mg_sales-catalog")
        property("sonar.organization", "groot-mg")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.java.coveragePlugin", "jacoco")
        property("sonar.exclusions", "**/*SalesCatalogApplication.kt, **/OpenApiConfiguration.kt")
    }
}