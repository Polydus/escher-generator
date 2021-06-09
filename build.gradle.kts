import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	maven
	id("org.springframework.boot") version "2.5.0"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.5.10"
	kotlin("plugin.spring") version "1.5.10"
	kotlin("plugin.serialization") version "1.5.10"
}

group = "com.polydus"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
	maven {
		setUrl("https://jitpack.io")
		setUrl("https://maven.imagej.net/content/repositories/public/")
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	//implementation("org.springframework.boot:spring-boot-starter-webflux")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

	//kotlin
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	//implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")

	//bootstrap
	implementation("org.webjars:bootstrap:5.0.1")
	implementation("org.webjars:jquery:3.6.0")
	implementation("org.webjars:font-awesome:5.15.2")
	implementation("org.webjars.npm:flag-icon-css:3.5.0")

	//devtools
	//implementation("org.springframework.boot:spring-boot-devtools")

	//delaunay
	implementation("com.github.jdiemke.delaunay-triangulator:DelaunayTriangulator:1.0.0")
	implementation(files("jar/poly2tri-core-0.1.1-SNAPSHOT.jar"))

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
