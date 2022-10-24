
buildscript {
	repositories {
		mavenCentral()
	}
	dependencies {
		classpath("org.springframework.boot:spring-boot-gradle-plugin:2.6.4")
	}
}

plugins {
	id("org.springframework.boot") version "2.6.4"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.4.32"
	kotlin("plugin.spring") version "1.4.32"
	kotlin("kapt") version "1.5.31"
}

group = "it.unibo.transporttrolleygui"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
	flatDir {
		dirs("unibolibs")
		dirs("jars")
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	implementation( "org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.0")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.0")

	implementation("org.eclipse.californium:californium-core:2.0.0-M12")
	implementation("org.eclipse.californium:californium-proxy:2.0.0-M12")

	implementation("tuprolog:2p301")
	implementation("qak:it.unibo.qakactor:2.4")
	implementation("uniboIssActorKotlin:IssActorKotlinRobotSupport:2.0")

	implementation("dsl:itunibo.automatedcarparking.dsl:1.0")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
