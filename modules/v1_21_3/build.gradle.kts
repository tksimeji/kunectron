plugins {
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.17"
}

dependencies {
    compileOnly(project(":modules:api"))
    compileOnly(project(":modules:v1_21_x"))
    paperweight.paperDevBundle("1.21.3-R0.1-SNAPSHOT")
}
