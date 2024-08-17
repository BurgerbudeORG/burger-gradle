# Burger Gradle

This Gradle plugin serves as a standardized foundation for all projects within the organization. It
provides a common set of configurations, tasks, and dependencies that streamline build processes,
enforce coding standards, and ensure consistency across all projects. By using this plugin, teams
can quickly set up new projects, reduce maintenance efforts, and align with the organization's best
practices.

### settings.gradle.kts

```kotlin
buildscript {
  repositories {
    maven("https://pommes.burgerbude.org/api/v1/maven/production/libraries/")
  }

  dependencies {
    classpath("org.burgerbude.gradle", "burger-plugin", "<VERSION>")
  }
}
```

### build.gradle.kts

```kotlin
plugins {
  id("org.burgerbude.burger")
}
```