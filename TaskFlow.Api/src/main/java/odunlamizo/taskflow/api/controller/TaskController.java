package odunlamizo.taskflow.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import odunlamizo.taskflow.api.config.ResourceServerConfig.AuthenticationFacade;
import odunlamizo.taskflow.api.model.Task;
import odunlamizo.taskflow.api.payload.request.TaskPayload;
import odunlamizo.taskflow.api.payload.request.TaskUpdatePayload;
import odunlamizo.taskflow.api.payload.response.AbstractResponsePayload;
import odunlamizo.taskflow.api.repository.TaskRepository;
import odunlamizo.taskflow.api.service.TaskService;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @GetMapping("")
    public ResponseEntity<AbstractResponsePayload<List<Task>>> getAllForAuthenticated() {
        Jwt jwt = (Jwt) authenticationFacade.getPrincipal();
        List<Task> tasks = taskRepository.findByOwner(jwt.getSubject());
        for(Task task : tasks) {
            task.setOwner(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(AbstractResponsePayload.success(tasks));
    }

    @PostMapping("/add")
    public ResponseEntity<AbstractResponsePayload<Task>> addTask(@RequestBody @Validated TaskPayload payload) {
        return ResponseEntity.status(HttpStatus.OK).body(AbstractResponsePayload.success(taskService.addTask(payload)));
    }

    @PutMapping("{id}/update")
    public ResponseEntity<AbstractResponsePayload<Task>> updateTask(@PathVariable long id,
            @RequestBody TaskUpdatePayload payload) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(AbstractResponsePayload.success(taskService.updateTask(id, payload)));
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<AbstractResponsePayload<Task>> deleteTask(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(AbstractResponsePayload.success(taskService.deleteTask(id)));
    }

}
