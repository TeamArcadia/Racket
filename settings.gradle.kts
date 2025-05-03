@file:Suppress("UnstableApiUsage") // unstable api를 사용해서도 경고 무시

rootProject.name = "Racket"

pluginManagement {
	val kotlinVersion: String by settings
	val shadowVersion: String by settings
	val paperWeightVersion: String by settings

	plugins {
		kotlin("jvm") version kotlinVersion apply false
		kotlin("plugin.serialization") version kotlinVersion apply false
		kotlin("kapt") version kotlinVersion apply false
		id("com.gradleup.shadow") version shadowVersion apply false

		id("io.papermc.paperweight.userdev") version paperWeightVersion apply false
	}

	repositories {
		mavenCentral()
		maven("https://jitpack.io")
		maven("https://repo.papermc.io/repository/maven-public/")
		gradlePluginPortal()
	}
}
plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

file(rootProject.projectDir.path + "/credentials.gradle.kts").let {
	if (it.exists()) {
		apply(it.path)
	}
}

dependencyResolutionManagement {
	repositories {
		mavenCentral()
		maven("https://repo.papermc.io/repository/maven-public/")
		maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
		maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
		maven("https://jitpack.io")
		maven("https://maven.fabricmc.net/")
	}

	versionCatalogs {
		create("libs") {
			library("spigot-api", "io.papermc.paper:paper-api:${getProperty("spigotVersion")}")
			library("paper-api", "io.papermc.paper:paper-api:${getProperty("spigotVersion")}")

			library("kotlin-stdlib-jdk8", "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${getProperty("kotlinVersion")}")
			library("kotlin-reflect", "org.jetbrains.kotlin:kotlin-reflect:${getProperty("kotlinVersion")}")
			library("kotlin-test", "org.jetbrains.kotlin:kotlin-test:${getProperty("kotlinVersion")}")

			library("kotlinx-serialization-json", "org.jetbrains.kotlinx:kotlinx-serialization-json:${getProperty("serializationVersion")}")

			library("netty", "io.netty:netty-all:${getProperty("nettyBufferVersion")}")

			library("snakeyaml", "org.yaml:snakeyaml:${getProperty("snakeYamlVersion")}")
			library("configurate-core", "org.spongepowered:configurate-core:${getProperty("configurateYamlVersion")}")
			library("configurate-yaml", "org.spongepowered:configurate-yaml:${getProperty("configurateYamlVersion")}")

			library("gson", "com.google.code.gson:gson:${getProperty("gsonVersion")}")
		}
	}
}

includeBuild("build-logic")
includeAll("modules")

fun includeAll(modulesDir: String) {
	file("${rootProject.projectDir.path}/${modulesDir.replace(":", "/")}/").listFiles()?.forEach { modulePath ->
		if (modulePath.name == ".DS_Store") {
			return@forEach
		}
		include("${modulesDir.replace("/", ":")}:${modulePath.name}")
	}
}

fun getProperty(key: String): String {
	return extra[key]?.toString() ?: throw IllegalArgumentException("property with $key not found")
}
