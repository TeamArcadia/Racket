plugins {
    id("racket.shared")
    id("racket.publish")
}

dependencies {
    api(libs.netty)

    implementation(project(":modules:racket-packet"))
}