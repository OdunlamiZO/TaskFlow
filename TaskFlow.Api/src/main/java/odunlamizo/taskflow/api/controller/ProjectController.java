package odunlamizo.taskflow.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import odunlamizo.taskflow.api.config.ResourceServerConfig.AuthenticationFacade;
import odunlamizo.taskflow.api.model.Project;
import odunlamizo.taskflow.api.payload.request.ProjectPayload;
import odunlamizo.taskflow.api.payload.response.AbstractResponsePayload;
import odunlamizo.taskflow.api.repository.ProjectRepository;
import odunlamizo.taskflow.api.service.ProjectService;

@RestController
@RequestMapping("/project")
public class ProjectController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private AuthenticationFacade authenticationFacade;

	@GetMapping("")
	public ResponseEntity<AbstractResponsePayload<List<?>>> getAllForAuthenticated(
			@RequestParam(required = false) String field) {
		Jwt jwt = (Jwt) authenticationFacade.getPrincipal();
		if (field != null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(AbstractResponsePayload.success(
							jdbcTemplate.queryForList(String.format(
									"SELECT %s FROM project WHERE owner = ?",
									field),
									String.class, jwt.getSubject())));
		}
		List<Project> projects = projectRepository.findByOwner(jwt.getSubject());
		for (Project project : projects) {
			project.setOwner(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(AbstractResponsePayload.success(projects));
	}

	@PostMapping("/add")
	public ResponseEntity<AbstractResponsePayload<Project>> addProject(
			@RequestBody @Validated ProjectPayload payload) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(AbstractResponsePayload.success(projectService.addProject(payload)));
	}

	@GetMapping("/{project}/progress")
	public ResponseEntity<AbstractResponsePayload<Float>> retrieveProgress(@PathVariable String project) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(AbstractResponsePayload.success(projectService.retrieveProgress(project)));
	}

	@PutMapping("{id}/update")
	public ResponseEntity<AbstractResponsePayload<Project>> updateProject(@PathVariable long id,
			@RequestParam String deadline) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(AbstractResponsePayload.success(projectService.updateProject(id, deadline)));
	}

	@DeleteMapping("{id}/delete")
	public ResponseEntity<AbstractResponsePayload<Project>> deleteProject(@PathVariable long id) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(AbstractResponsePayload.success(projectService.deleteProject(id)));
	}

}
