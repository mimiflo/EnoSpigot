rootProject.name = "enospigot"

includeBuild("build-logic")

fun setupSubproject(name: String, dir: String) {
    include(":$name")
    project(":$name").projectDir = file(dir)
}

setupSubproject("pandaspigot-server", "EnoSpigot-Server")
setupSubproject("pandaspigot-api", "EnoSpigot-API")
setupSubproject("paperclip", "paperclip")
