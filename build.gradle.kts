plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    mainClass = "org.example.fitness.Application"
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<JavaCompile>("compileJava") {
    options.compilerArgs.add("-Xlint:unchecked")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
