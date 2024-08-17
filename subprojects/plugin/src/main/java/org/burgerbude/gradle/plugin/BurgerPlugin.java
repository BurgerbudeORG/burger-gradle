package org.burgerbude.gradle.plugin;

import org.burgerbude.gradle.plugin.maven.MavenConfigurator;
import org.burgerbude.gradle.plugin.settings.BurgerSettings;
import org.burgerbude.gradle.plugin.util.GradleUtil;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.initialization.Settings;
import org.gradle.api.plugins.JavaLibraryPlugin;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.testing.Test;
import org.jetbrains.annotations.NotNull;

public class BurgerPlugin implements Plugin<Object> {

  public static final String BASE_URL = "https://pommes.burgerbude.org/api/v1/maven/";
  private final MavenConfigurator mavenPlugin = new MavenConfigurator();
  private Project project;

  @Override
  public void apply(final @NotNull Object target) {
    if (target instanceof Settings settingsTarget) {
      this.applySettings(settingsTarget);
    } else if (target instanceof Project projectTarget) {
      this.applyProject(projectTarget);
    } else {
      throw new UnsupportedOperationException("Unsupported target: " + target);
    }
  }

  private void applySettings(final @NotNull Settings settings) {
    settings.getExtensions().create("burgerSettings", BurgerSettings.class, settings);
  }

  private void applyProject(final @NotNull Project project) {
    project.getPluginManager().apply(JavaLibraryPlugin.class);

    this.project = project;
    project.setVersion(GradleUtil.getVersion(project));

    this.configureRepositories();
    this.configureTestEnvironment();

    this.mavenPlugin.apply(project);
  }

  private void configureRepositories() {
    RepositoryHandler repositories = this.project.getRepositories();
    repositories.maven(repository -> {
      repository.setName("Burgerbude");
      repository.setUrl(BASE_URL + "production/libraries/");
    });
    repositories.mavenCentral();
  }

  private void configureTestEnvironment() {
    DependencyHandler dependencies = this.project.getDependencies();
    dependencies.add(JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME, dependencies.platform("org.junit:junit-bom:5.11.0"));
    dependencies.add(JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME, "org.junit.jupiter:junit-jupiter");

    this.project.getTasks().withType(Test.class, Test::useJUnitPlatform);
  }

}
