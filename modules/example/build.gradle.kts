plugins {
    id("racket.shared")
    id("racket.publish")
}

dependencies {
    apiModule("core")
    apiModule("server")
    apiModule("client")
}