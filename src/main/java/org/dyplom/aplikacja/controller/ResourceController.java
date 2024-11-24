package org.dyplom.aplikacja.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.dyplom.aplikacja.logic.ResourceService;
import org.dyplom.aplikacja.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Resource Controller")
@RestController
@RequestMapping("/resources")
public class ResourceController {

  @Autowired
  private ResourceService resourceService;

  @GetMapping
  public ResponseEntity<List<Resource>> getAllResources() {
    return ResponseEntity.ok(resourceService.getAllResources());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Resource> getResourceById(@PathVariable Long id) {
    return ResponseEntity.ok(resourceService.getResourceById(id));
  }

  @PostMapping
  public ResponseEntity<?> createResource(@RequestBody Resource resource) {
    try {
      Resource createdResource = resourceService.createResource(resource);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdResource);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating resource: " + e.getMessage());
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateResource(@PathVariable Long id, @RequestBody Resource resource) {
    try {
      Resource updatedResource = resourceService.updateResource(id, resource);
      return ResponseEntity.ok(updatedResource);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating resource: " + e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
    resourceService.deleteResource(id);
    return ResponseEntity.noContent().build();
  }
}


