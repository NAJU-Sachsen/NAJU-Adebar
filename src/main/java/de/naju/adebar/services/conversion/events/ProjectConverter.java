package de.naju.adebar.services.conversion.events;

import de.naju.adebar.model.chapter.Project;
import de.naju.adebar.model.chapter.ReadOnlyProjectRepository;
import javax.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class ProjectConverter implements Converter<String, Project> {

  private final ReadOnlyProjectRepository projectRepo;

  public ProjectConverter(ReadOnlyProjectRepository projectRepo) {
    Assert.notNull(projectRepo, "projectRepo may not be null");
    this.projectRepo = projectRepo;
  }

  @Override
  public Project convert(@Nonnull String source) {
    if (source.isEmpty()) {
      return null;
    }
    return projectRepo.findById(Long.parseLong(source)).orElse(null);
  }
}
