rootProject.name = "kunectron"
include(
    ":modules:api",
    ":modules:core",
    ":modules:v1_21_1",
    ":modules:v1_21_3",
    ":modules:v1_21_x"
)
include("modules:v1_21_5")
findProject(":modules:v1_21_5")?.name = "v1_21_5"
