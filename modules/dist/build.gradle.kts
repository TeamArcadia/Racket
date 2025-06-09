plugins {
    kotlin("jvm")
    id("com.gradleup.shadow")
    id("racket.shared")
    `maven-publish`
}

dependencies {
    implementationModule("core")
    implementationModule("server")
    implementationModule("client")
}

tasks {
    shadowJar {
        // 최종 JAR 파일 이름 설정
        archiveBaseName.set("racket-dist")
        archiveClassifier.set("")
        archiveVersion.set(project.version.toString())

        // 메타데이터 병합
        mergeServiceFiles()
    }
}

// shadowJar를 기본 jar 대신 사용
artifacts {
    archives(tasks.shadowJar)
}

publishing {
    publications {
        create<MavenPublication>("shadow") {
            project.shadow.component(this)

            groupId = project.group.toString()
            artifactId = "racket-all"
            version = project.version.toString()
        }
    }
}
