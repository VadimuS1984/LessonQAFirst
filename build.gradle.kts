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
}

//tasks.withType<Test> {
//    useJUnitPlatform()
//}
