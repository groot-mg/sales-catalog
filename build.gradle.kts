plugins {
    kotlin("jvm") version "1.8.10" apply false
    kotlin("plugin.spring") version "1.8.10" apply false
    id("io.freefair.lombok") version "8.0.1" apply false
    id("org.sonarqube") version "4.0.0.2929"
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