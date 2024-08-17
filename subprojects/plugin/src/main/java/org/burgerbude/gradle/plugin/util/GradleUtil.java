package org.burgerbude.gradle.plugin.util;

import org.gradle.api.Project;
import org.gradle.api.provider.ProviderFactory;

public final class GradleUtil {

  private static final String DEFAULT_VERSION = "1.0.0-SNAPSHOT";
  private static final String ENV_VERSION_KEY = "VERSION";
  private static final String PROPERTY_VERSION_KEY = "version";

  public static String getVersion(final Project project) {
    ProviderFactory providers = project.getProviders();
    return providers.environmentVariable(ENV_VERSION_KEY).getOrElse(getPropertyVersion(project));
  }

  private static String getPropertyVersion(final Project project) {
    if (project.hasProperty(PROPERTY_VERSION_KEY)) {
      return String.valueOf(project.property(PROPERTY_VERSION_KEY));
    }

    return DEFAULT_VERSION;
  }

}
