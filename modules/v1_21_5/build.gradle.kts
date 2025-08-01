plugins {
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.18"
}

dependencies {
    compileOnly(project(":modules:api"))
    paperweight.paperDevBundle("1.21.5-R0.1-SNAPSHOT")
}
