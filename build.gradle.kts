subprojects {
  plugins.apply("java-library")
  plugins.apply("maven-publish")

  group = "org.burgerbude.gradle"
  version = providers.environmentVariable("VERSION").getOrElse("0.1.0-local")

  repositories {
    mavenCentral()
  }

  dependencies {
    "testImplementation"(platform("org.junit:junit-bom:5.10.0"))
    "testImplementation"("org.junit.jupiter:junit-jupiter")
  }

  tasks.withType(Test::class).configureEach {
    useJUnitPlatform()
  }

  extensions.getByType(PublishingExtension::class).apply {
    publications {
      create<MavenPublication>("maven") {
        groupId = group.toString()
        artifactId = project.name
        version = version

        from(components["java"])
      }
    }

    repositories {
      val publishToken = providers.environmentVariable("PUBLISH_TOKEN")
      if (publishToken.isPresent) {
        maven {
          name = "Burgerbude"
          url = uri("https://pommes.burgerbude.org/api/v1/maven/upload")

          authentication {
            create<HttpHeaderAuthentication>("header")
          }

          credentials(HttpHeaderCredentials::class) {
            name = "Publish-Token"
            value = publishToken.get()
          }
        }
      }
    }
  }
}
