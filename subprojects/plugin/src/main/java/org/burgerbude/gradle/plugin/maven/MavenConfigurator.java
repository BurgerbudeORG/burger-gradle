package org.burgerbude.gradle.plugin.maven;

import org.burgerbude.gradle.plugin.BurgerPlugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.credentials.HttpHeaderCredentials;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.publish.PublicationContainer;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin;
import org.gradle.authentication.http.HttpHeaderAuthentication;
import org.jetbrains.annotations.NotNull;

public class MavenConfigurator {

  private Project project;

  public MavenConfigurator() {
  }

  public void apply(final Project target) {
    if (!target.getPlugins().hasPlugin(MavenPublishPlugin.class)) {
      return;
    }

    this.project = target;

    target.getExtensions().configure(PublishingExtension.class, publishing -> {
      publishing.publications(this::createMavenPublication);
      publishing.repositories(this::createMavenRepository);
    });
  }

  private void createMavenPublication(final @NotNull PublicationContainer container) {
    container.create("burger", MavenPublication.class, publication -> {
      publication.setGroupId(String.valueOf(this.project.getGroup()));
      publication.setArtifactId(this.project.getName());
      publication.setVersion(this.project.getVersion().toString());

      publication.from(this.project.getComponents().getByName("java"));
    });
  }

  private void createMavenRepository(final @NotNull RepositoryHandler handler) {
    ProviderFactory providers = this.project.getProviders();
    Provider<String> publishToken = providers.environmentVariable("PUBLISH_TOKEN");
    if (!publishToken.isPresent()) {
      return;
    }

    handler.maven(repository -> {
      repository.setName("Burgerbude");
      repository.setUrl(BurgerPlugin.BASE_URL + "upload/");

      repository.authentication(authentications -> authentications.create("header", HttpHeaderAuthentication.class));

      repository.credentials(HttpHeaderCredentials.class, credentials -> {
        credentials.setName("Publish-Token");
        credentials.setValue(publishToken.get());
      });
    });
  }
}
