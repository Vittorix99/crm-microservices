import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.23"
	kotlin("plugin.spring") version "1.9.23"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.cloud:spring-cloud-starter-gateway-mvc:4.1.1")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("com.fasterxml.jackson.core:jackson-databind")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	implementation ("org.springframework.boot:spring-boot-starter-security")
	implementation ("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

	implementation("org.apache.camel:camel-core:3.14.0")
	implementation("com.google.api-client:google-api-client:1.32.1")
	implementation("org.apache.camel:camel-gson:4.5.0")
	implementation("org.apache.camel:camel-mail:3.11.0")
	implementation("org.apache.camel:camel-http:4.5.0")
	implementation("org.apache.camel:camel-jackson:4.5.0")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.apache.camel.springboot:camel-spring-boot-starter:4.5.0")
	implementation("org.apache.camel.springboot:camel-google-mail-starter:4.5.0")
	implementation("com.google.apis:google-api-services-gmail:v1-rev110-1.25.0")
	implementation("org.apache.camel:camel-google-mail:4.5.0")
	implementation("javax.mail:javax.mail-api:1.6.2")  // Add this dependency for javax.mail

	implementation("org.springframework.boot:spring-boot-starter-validation")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:postgresql")
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(module = "mockito-core")
		exclude(module = "junit")
	}
	testImplementation("org.junit.jupiter:junit-jupiter:5.8.0")
	testImplementation("com.ninja-squad:springmockk:4.0.2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:postgresql")

	// Camel test support for Spring with JUnit 5
	testImplementation("org.apache.camel:camel-test-spring-junit5:4.3.0")

	// JUnit Vintage Engine for running JUnit 4 tests with JUnit 5
	testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.7.2")
	implementation("org.apache.camel:camel-test-junit5:4.6.0")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<BootJar> {
	duplicatesStrategy = DuplicatesStrategy.INCLUDE
}