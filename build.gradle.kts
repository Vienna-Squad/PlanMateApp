import org.gradle.declarative.dsl.schema.FqName.Empty.packageName
import java.io.FileInputStream
import java.util.*

plugins {
    kotlin("jvm") version "2.1.0"
    id("com.github.gmazzo.buildconfig") version "4.1.2"
    jacoco
}
val localProperties = Properties().apply {
    val localFile = rootProject.file("keys.properties")
    if (localFile.exists()) {
        FileInputStream(localFile).use { load(it) }
    }
}
val mongoUri = localProperties.getProperty("MONGO.URI")
val databaseName = localProperties.getProperty("DATABASE.NAME")

buildConfig {
    packageName("org.example")
    buildConfigField("String", "MONGO_URI", "\"${mongoUri}\"")
    buildConfigField("String", "DATABASE_NAME", "\"${databaseName}\"")
    buildConfigField("String", "APP_VERSION", "\"${project.version}\"")
}
jacoco {
    toolVersion = "0.8.7"
}
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    exclude("**/test/**") // Excludes all test packages
    // or be more specific
    // exclude("com/example/mypackage/test/**")
}
fun findTestedProductionClasses(): List<String> {
    val testFiles = fileTree("src/test/kotlin") {
        include("**/*Test.kt")
    }.files
    return testFiles.map { file ->
        val relativePath = file.relativeTo(file("src/test/kotlin")).path
            .removeSuffix("Test.kt")
            .replace("\\", "/")
        "**/${relativePath}.class"
    }
}
tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}
tasks.jacocoTestReport {
    dependsOn(tasks.test)
    val includedClasses = findTestedProductionClasses()
    classDirectories.setFrom(
        fileTree("$buildDir/classes/kotlin/main") {
            include(includedClasses)
        }
    )
    sourceDirectories.setFrom(files("src/main/kotlin"))
    doFirst {
        println("=== INCLUDED PRODUCTION CLASSES ===")
        includedClasses.forEach {
            println(it)
        }
    }
    reports {
        html.required.set(true)
        xml.required.set(true)
    }
}
tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.jacocoTestReport)

    // Use the same includedClasses for verification as in the report
    val includedClasses = findTestedProductionClasses()

    classDirectories.setFrom(
        fileTree("$buildDir/classes/kotlin/main") {
            include(includedClasses)
        }
    )
    violationRules {
        rule {
            // Generic instruction coverage (default counter)
            limit {
                minimum = "0.8".toBigDecimal()
            }

            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.8".toBigDecimal()
            }

            limit {
                counter = "METHOD"
                value = "COVEREDRATIO"
                minimum = "0.8".toBigDecimal()
            }
            limit {
                counter = "CLASS"
                value = "COVEREDRATIO"
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
    testImplementation(kotlin("test"))
    implementation("io.insert-koin:koin-core:4.0.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("io.mockk:mockk:1.13.10")
    testImplementation("com.google.truth:truth:1.4.2")
    // MongoDB driver
    implementation("org.mongodb:mongodb-driver-sync:4.9.1")

    // Kotlin serialization for MongoDB (optional but helpful)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events ("passed", "skipped", "failed")
        showStandardStreams = true
    }
}
