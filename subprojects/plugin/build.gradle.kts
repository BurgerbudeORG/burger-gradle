plugins {
  id("java-gradle-plugin")
  `kotlin-dsl`
}

gradlePlugin {
  plugins {
    create("burger") {
      id = "org.burgerbude.burger"
      implementationClass = "org.burgerbude.gradle.plugin.BurgerPlugin"
    }
  }
}