package odunlamizo.taskflow.api.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import odunlamizo.taskflow.api.TaskFlowRuntimeException;
import odunlamizo.taskflow.api.config.ResourceServerConfig.AuthenticationFacade;
import odunlamizo.taskflow.api.dto.Error.Category;
import odunlamizo.taskflow.api.model.Project;
import odunlamizo.taskflow.api.model.Status;
import odunlamizo.taskflow.api.model.Task;
import odunlamizo.taskflow.api.payload.request.ProjectPayload;
import odunlamizo.taskflow.api.repository.ProjectRepository;
import odunlamizo.taskflow.api.repository.TaskRepository;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Override
    public Project addProject(ProjectPayload payload) {
        Jwt jwt = (Jwt) authenticationFacade.getPrincipal();
        Project project = new Project();
        project.setOwner(jwt.getSubject());
        project.setTitle(payload.getTitle());
        String deadline = payload.getDeadline();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        project.setDeadline(deadline != null ? LocalDate.parse(deadline, formatter) : null);
        return projectRepository.save(project);
    }

    @Override
    public Float retrieveProgress(String project) {
        Jwt jwt = (Jwt) authenticationFacade.getPrincipal();
        List<Task> tasks = taskRepository.findByProject(project, jwt.getSubject());
        if (tasks.isEmpty()) {
            return (float) 0.0;
        }
        return ((float) tasks.stream().filter(task -> task.getStatus() == Status.COMPLETED).collect(Collectors.toList())
                .size() / tasks.size()) * 100;
    }

    @Override
    public Project updateProject(long id, String deadline) {
        Optional<Project> _project = projectRepository.findById(id);
        if (_project.isEmpty()) {
            throw TaskFlowRuntimeException.with(Category.MISSING_RESOURCE, "Project not found");
        }
        Project project = _project.get();
        Jwt jwt = (Jwt) authenticationFacade.getPrincipal();
        if (!project.getOwner().equals(jwt.getSubject())) {
            throw TaskFlowRuntimeException.with(Category.BAD_REQUEST);
        }
        if (deadline != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            project.setDeadline(LocalDate.parse(deadline, formatter));
        }
        return projectRepository.save(project);
    }

    @Override
    public Project deleteProject(long id) {
        Optional<Project> _project = projectRepository.findById(id);
        if (_project.isEmpty()) {
            throw TaskFlowRuntimeException.with(Category.MISSING_RESOURCE, "Project not found");
        }
        Project project = _project.get();
        Jwt jwt = (Jwt) authenticationFacade.getPrincipal();
        if (!project.getOwner().equals(jwt.getSubject())) {
            throw TaskFlowRuntimeException.with(Category.BAD_REQUEST);
        }
        projectRepository.delete(project);
        for(Task task : taskRepository.findByProject(project.getTitle(), jwt.getSubject())) {
            taskRepository.delete(task);
        }
        return project;
    }

}
