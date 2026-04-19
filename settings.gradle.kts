rootProject.name = "enospigot"

includeBuild("build-logic")

fun setupSubproject(name: String, dir: String) {
    include(":$name")
    project(":$name").projectDir = file(dir)
}

setupSubproject("enospigot-server", "EnoSpigot-Server")
setupSubproject("enospigot-api", "EnoSpigot-API")
setupSubproject("paperclip", "paperclip")
