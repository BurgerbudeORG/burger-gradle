package org.burgerbude.gradle.plugin.settings;

import org.gradle.api.initialization.ProjectDescriptor;
import org.gradle.api.initialization.Settings;
import org.gradle.api.provider.Property;

import java.io.File;

public abstract class BurgerSettings {

  private final Settings settings;

  public BurgerSettings(final Settings settings) {
    this.settings = settings;
  }

  public void defineModule(final String... paths) {
    for (final String path : paths) {
      this.defineModule(path);
    }
  }

  public void defineModule(final String path) {
    this.settings.include(":" + path);
    ProjectDescriptor selectedProject = this.settings.findProject(":" + path);
    if (selectedProject == null) {
      return;
    }

    String projectName = path;
    if (this.getIsRootProjectPrefix().getOrElse(false)) {
      projectName = this.settings.getRootProject().getName() + "-" + path;
    }

    selectedProject.setName(projectName);
    selectedProject.setProjectDir(new File("subprojects/" + path));
    File projectDir = selectedProject.getProjectDir();
    if (!projectDir.exists()) {
      projectDir.mkdirs();
    }
  }

  public abstract Property<Boolean> getIsRootProjectPrefix();

}
