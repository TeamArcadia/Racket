import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    `kotlin-dsl`
    `maven-publish`
}

val properties = loadProperties(rootProject.gradle.parent?.rootProject?.projectDir?.path + "/gradle.properties")
val kotlinVersion: String = properties.getProperty("kotlinVersion")
val shadowPluginVersion: String = properties.getProperty("shadowVersion")

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
    implementation("com.gradleup.shadow:shadow-gradle-plugin:$shadowPluginVersion")
    api(gradleApi())
}

gradlePlugin {
    plugins {
        register("racket-dependency-handler-extensions") {
            id = "racket.dependency-handler-extensions"
            implementationClass = "DependencyHandlerExtensions"
        }
        register("racket-runtime-dependency-relocator") {
            id = "racket.runtime-dependency-relocator"
            implementationClass = "RuntimeDependencyRelocator"
        }
    }
}