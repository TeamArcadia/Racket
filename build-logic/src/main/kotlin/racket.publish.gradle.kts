plugins {
	`maven-publish`

}


file(rootProject.gradle.rootProject.projectDir.path + "/credentials.gradle.kts").let {
	if (it.exists()) {
		apply(it.path)
	}
}

val sourcesJar by tasks.registering(Jar::class) {
	from((project.extensions.getByName("sourceSets") as SourceSetContainer)["main"].allSource)
	archiveClassifier.set("sources")
}

publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			groupId = project.extra["projectGroup"]!!.toString()
			artifactId = project.project.name.lowercase()
			version = project.extra["projectVersion"]!!.toString()

			from(components["java"])

			artifact(sourcesJar.get()) {
				this.classifier = "sources"
			}

			pom {
				name.set(extra["projectName"]?.toString())
				url.set(extra["projectUrl"]?.toString())
			}
		}
	}
}