import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

class DependencyHandlerExtensions : Plugin<Project> {
	override fun apply(target: Project) {}
}

fun DependencyHandler.runtimeOnlyModule(module: String): Dependency? =
	add("runtimeOnly", project(":modules:$module"))

fun DependencyHandler.implementationModule(module: String): Dependency? =
	add("implementation", project(":modules:$module"))

fun DependencyHandler.apiModule(module: String): Dependency? =
	add("api", project(":modules:$module"))

fun DependencyHandler.testImplementationModule(module: String): Dependency? =
	add("testImplementation", project(":modules:$module"))