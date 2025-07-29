plugins {
    java
    id("org.springframework.boot") version "3.5.2"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	runtimeOnly("org.postgresql:postgresql")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Jackson
    implementation("com.fasterxml.jackson.core:jackson-databind:2.19.0")

    // https://mvnrepository.com/artifact/com.github.javafaker/javafaker fixme не работает
//    implementation("com.github.javafaker:javafaker:1.0.2")

    // Log
    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("ch.qos.logback:logback-classic:1.5.18")
    implementation("ch.qos.logback:logback-core:1.5.18")

    // Apache Commons Lang для RandomStringUtils
    testImplementation("org.apache.commons:commons-lang3:3.12.0")

    // Rest Assured
    implementation("io.rest-assured:rest-assured:5.4.0")
    testImplementation("io.rest-assured:rest-assured:5.4.0")
    testImplementation("io.rest-assured:json-path:5.4.0")
    testImplementation("io.rest-assured:xml-path:5.4.0")

    // TestNG
    testImplementation("org.testng:testng:7.11.0")

    // Hamcrest
    testImplementation("org.hamcrest:hamcrest:2.2")
}

tasks.test {
    useTestNG()  // Используйте TestNG вместо JUnit

    testLogging {
//        events("PASSE", "FAILED", "SKIPPED") fixme непонятная ошибка
        showStandardStreams = true
//        exceptionFormat = "full"
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
//    test.outputs.upToDateWhen { false }  //fixme не работает
    //если код (output ы) не менялся то gradle код не меняет,
    // сейчас говорим что б он игнариловал outputs
}


//tasks.withType<Test> {
//    useJUnitPlatform()
//}
