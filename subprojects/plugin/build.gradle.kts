plugins {
  id("java-gradle-plugin")
}

gradlePlugin {
  plugins {
    create("burger") {
      id = "org.burgerbude.burger"
      implementationClass = "org.burgerbude.gradle.plugin.BurgerPlugin"
    }
  }
}