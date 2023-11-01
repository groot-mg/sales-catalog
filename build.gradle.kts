plugins {
    kotlin("jvm") version "1.9.20" apply false
    kotlin("plugin.spring") version "1.9.20" apply false
    id("io.freefair.lombok") version "8.4" apply false
}

subprojects {
    group = "com.generoso"
}