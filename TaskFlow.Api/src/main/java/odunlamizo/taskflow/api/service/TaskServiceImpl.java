package odunlamizo.taskflow.api.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import odunlamizo.taskflow.api.TaskFlowRuntimeException;
import odunlamizo.taskflow.api.config.ResourceServerConfig.AuthenticationFacade;
import odunlamizo.taskflow.api.dto.Error.Category;
import odunlamizo.taskflow.api.model.Status;
import odunlamizo.taskflow.api.model.Task;
import odunlamizo.taskflow.api.payload.request.TaskPayload;
import odunlamizo.taskflow.api.payload.request.TaskUpdatePayload;
import odunlamizo.taskflow.api.repository.TaskRepository;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Override
    public Task addTask(TaskPayload payload) {
        Jwt jwt = (Jwt) authenticationFacade.getPrincipal();
        Task task = new Task();
        task.setOwner(jwt.getSubject());
        task.setDescription(payload.getDescription());
        task.setProject(payload.getProject());
        task.setStatus(Status.PENDING);
        String deadline = payload.getDeadline();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        task.setDeadline(deadline != null ? LocalDate.parse(deadline, formatter) : null);
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(long id, TaskUpdatePayload payload) {
        Optional<Task> _task = taskRepository.findById(id);
        if (_task.isEmpty()) {
            throw TaskFlowRuntimeException.with(Category.MISSING_RESOURCE, "Task not found");
        }
        Task task = _task.get();
        Jwt jwt = (Jwt) authenticationFacade.getPrincipal();
        if (!task.getOwner().equals(jwt.getSubject())) {
            throw TaskFlowRuntimeException.with(Category.BAD_REQUEST);
        }
        if (payload.getStatus() != null) {
            task.setStatus(payload.getStatus());
        }
        if (payload.getDeadline() != null) {
            String deadline = payload.getDeadline();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            task.setDeadline(LocalDate.parse(deadline, formatter));
        }
        return taskRepository.save(task);
    }

    @Override
    public Task deleteTask(long id) {
        Optional<Task> _task = taskRepository.findById(id);
        if (_task.isEmpty()) {
            throw TaskFlowRuntimeException.with(Category.MISSING_RESOURCE, "Task not found");
        }
        Task task = _task.get();
        Jwt jwt = (Jwt) authenticationFacade.getPrincipal();
        if (!task.getOwner().equals(jwt.getSubject())) {
            throw TaskFlowRuntimeException.with(Category.BAD_REQUEST);
        }
        taskRepository.delete(task);
        return task;
    }

}
