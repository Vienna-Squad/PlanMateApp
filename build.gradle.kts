plugins {
    kotlin("jvm") version "2.1.20"
    jacoco
}
jacoco {
    toolVersion = "0.8.10"
}
tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}
tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.8".toBigDecimal()
            }
        }
    }
}
tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.insert-koin:koin-core:4.0.2")
    testImplementation(kotlin("test"))
    testImplementation ("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation ("io.mockk:mockk:1.13.10")
    testImplementation ("com.google.truth:truth:1.4.2")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}