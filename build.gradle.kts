plugins {
    kotlin("jvm") version "1.7.20" apply false
    kotlin("plugin.spring") version "1.7.20" apply false
    id("io.freefair.lombok") version "6.5.1" apply false
    id("org.sonarqube") version "3.4.0.2513"
}

subprojects {
    group = "com.generoso"
}

sonarqube {
    properties {
        property("sonar.projectKey", "groot-mg_sales-catalog")
        property("sonar.organization", "groot-mg")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.java.coveragePlugin", "jacoco")
        property("sonar.exclusions", "**/*SalesCatalogApplication.kt, **/*Config.kt")
    }
}