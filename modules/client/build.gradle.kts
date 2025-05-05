plugins {
    id("racket.shared")
    id("racket.publish")
}

dependencies {
    api(libs.netty)
    apiModule("core")
}