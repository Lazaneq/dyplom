package org.dyplom.aplikacja.logic;

import java.util.List;
import org.dyplom.aplikacja.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceService {

  @Autowired
  private ResourceRepository resourceRepository;

  public List<Resource> getAllResources() {
    return resourceRepository.findAll();
  }

  public Resource getResourceById(Long id) {
    return resourceRepository.findById(id).orElse(null);
  }

  public Resource createResource(Resource resource) {
    return resourceRepository.save(resource);
  }

  public Resource updateResource(Long id, Resource resource) {
    resource.setId(id);
    return resourceRepository.save(resource);
  }

  public void deleteResource(Long id) {
    resourceRepository.deleteById(id);
  }
}

