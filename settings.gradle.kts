rootProject.name = "burger"

defineModule("plugin")

fun defineModule(vararg paths: String) {
  paths.forEach { defineModule(it) }
}

fun defineModule(path: String) {
  include(":$path")
  findProject(":$path")?.apply {
    name = "${rootProject.name}-$path"
    projectDir = file("subprojects/$path")
    if (!projectDir.exists()) {
      projectDir.mkdirs()
    }
  }
}