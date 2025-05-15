plugins {
    id("racket.shared")
    id("racket.publish")
    id("racket.shadow")
}

dependencies {
    api(libs.netty)
    apiModule("core")
}