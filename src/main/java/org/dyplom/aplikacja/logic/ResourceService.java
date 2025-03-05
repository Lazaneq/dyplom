package org.dyplom.aplikacja.logic;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.dyplom.aplikacja.exceptions.ResourceNotFoundException;
import org.dyplom.aplikacja.logic.repositories.ResourceRepository;
import org.dyplom.aplikacja.logic.repositories.TaskRepository;
import org.dyplom.aplikacja.model.DTO.ResourceDTO;
import org.dyplom.aplikacja.model.DTO.TaskDTO;
import org.dyplom.aplikacja.model.Resource;
import org.dyplom.aplikacja.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceService {

  @Autowired
  private ResourceRepository resourceRepository;

  @Autowired
  private TaskRepository taskRepository;

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

  public void assignResourcesToOrder(Long taskId, List<Long> resourceIds) {
    Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found"));

    for (Long resourceId : resourceIds) {
      Resource resource = resourceRepository.findById(resourceId).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

      if ("IN USE".equals(resource.getStatus())) {
        throw new IllegalStateException("Resource " + resource.getName() + " is already in use.");
      }

      task.getResources().add(resource);
      resource.getTasks().add(task);
      resource.setStatus("IN USE");
      resourceRepository.save(resource);
    }

    taskRepository.save(task);
  }

  public ResourceDTO mapToDTO(Resource resource) {
    ResourceDTO resourceDTO = new ResourceDTO();
    resourceDTO.setId(resource.getId());
    resourceDTO.setName(resource.getName());
    resourceDTO.setDescription(resource.getDescription());
    resourceDTO.setType(resource.getType());
    resourceDTO.setSerialNumber(resource.getSerialNumber());
    resourceDTO.setStatus(resource.getStatus());

    Set<TaskDTO> taskDTOs = resource.getTasks().stream()
        .map(task -> new TaskDTO(task.getId(), task.getTitle(), task.getStatus()))
        .collect(Collectors.toSet());

    resourceDTO.setAssignedTasks(taskDTOs);
    return resourceDTO;
  }
}

