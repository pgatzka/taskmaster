package io.github.pgatzka.taskmaster.rest.controller;

import io.github.pgatzka.taskmaster.rest.controller.service.TaskControllerService;
import io.github.pgatzka.taskmaster.rest.model.TaskModel;
import io.github.pgatzka.taskmaster.rest.request.CreateTaskRequest;
import io.github.pgatzka.taskmaster.rest.request.TaskFilterRequest;
import io.github.pgatzka.taskmaster.rest.request.UpdateTaskRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskControllerService service;

    @PostMapping
    ResponseEntity<TaskModel> create(@Valid @RequestBody CreateTaskRequest request) {
        TaskModel task = service.create(request);
        return ResponseEntity.created(MvcUriComponentsBuilder.fromMethodCall(MvcUriComponentsBuilder.controller(TaskController.class).get(task.key())).build().toUri()).body(task);
    }

    @GetMapping("/{key}")
    ResponseEntity<TaskModel> get(@PathVariable UUID key) {
        return ResponseEntity.ok(service.get(key));
    }

    @GetMapping
    ResponseEntity<PagedModel<TaskModel>> get(@Valid @ModelAttribute TaskFilterRequest filter, Pageable pageable) {
        return ResponseEntity.ok(service.get(filter, pageable));
    }

    @PutMapping("/{key}")
    ResponseEntity<TaskModel> update(@PathVariable UUID key, @RequestParam Long version, @Valid @RequestBody UpdateTaskRequest request) {
        return ResponseEntity.ok(service.update(key, version, request));
    }

    @DeleteMapping("/{key}")
    ResponseEntity<Void> delete(@PathVariable UUID key, @RequestParam Long version) {
        service.delete(key, version);
        return ResponseEntity.noContent().build();
    }

}
