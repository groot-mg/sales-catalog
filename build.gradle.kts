plugins {
    kotlin("jvm") version "1.8.21" apply false
    kotlin("plugin.spring") version "1.9.10" apply false
    id("io.freefair.lombok") version "8.0.1" apply false
}

subprojects {
    group = "com.generoso"
}