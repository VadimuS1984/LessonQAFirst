//plugins {
//    java
//    id("org.springframework.boot") version "3.5.2"
//    id("io.spring.dependency-management") version "1.1.7"
//}
//
//group = "com.example"
//version = "0.0.1-SNAPSHOT"
//
//repositories {
//    mavenCentral()
//}
//
//// Настройки для всех подпроектов
//subprojects {
//    apply(plugin = "java")
//
//    java {
//        toolchain {
//            languageVersion = JavaLanguageVersion.of(21)
//        }
//    }
//
//    configurations {
//        compileOnly {
//            extendsFrom(configurations.annotationProcessor.get())
//        }
//    }
//// Общие зависимости для всех модулей
//    dependencies {
//        // Spring Boot
////        implementation("org.springframework:spring-web:6.2.9")
////        implementation("org.springframework.boot:spring-boot-starter-web")
////        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
////        testImplementation("org.springframework.boot:spring-boot-starter-test")
//
//
//        // https://mvnrepository.com/artifact/com.github.javafaker/javafaker fixme не работает
//        implementation("com.github.javafaker:javafaker:1.0.2")
//
//        // Logging
//        implementation("org.slf4j:slf4j-api:2.0.17")
//        implementation("ch.qos.logback:logback-classic:1.5.18")
//        implementation("ch.qos.logback:logback-core:1.5.18")
//
//        // Testing
//        // Apache Commons Lang для RandomStringUtils
//        testImplementation("org.apache.commons:commons-lang3:3.12.0")
//
//        // TestNG
//        testImplementation("org.testng:testng:7.11.0")
//
//        // Hamcrest
//        testImplementation("org.hamcrest:hamcrest:2.2")
//
//// Selenide для UI тестов
////    implementation("com.codeborne:selenide:7.9.4")
//
//
//    }
//}
//
//// Зависимости только для основного приложения
//dependencies {
//    // Spring Boot
////    implementation("org.springframework.boot:spring-boot-starter-web")
////    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
////    testImplementation("org.springframework.boot:spring-boot-starter-test")
//
//    // Database
//    runtimeOnly("org.postgresql:postgresql")
//
//    // Lombok
//    compileOnly("org.projectlombok:lombok")
//    annotationProcessor("org.projectlombok:lombok")
//}
//tasks.test {
//    useTestNG()  // Используйте TestNG вместо JUnit
//
//    testLogging {
//        showStandardStreams = true
//        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
//        events("PASSED", "FAILED", "SKIPPED")
//    }
//    // Игнорировать кэш для тестов
//    outputs.upToDateWhen { false }  //fixme не работает
//    //если код (output ы) не менялся то gradle код не меняет,
//    // сейчас говорим что б он игнариловал outputs
//}
