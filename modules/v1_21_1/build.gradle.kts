plugins {
    id("io.papermc.paperweight.userdev") version "1.7.7"
}

dependencies {
    compileOnly(project(":modules:api"))
    compileOnly(project(":modules:v1_21_x"))
    paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")
}
