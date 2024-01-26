package odunlamizo.taskflow.api.service;

import odunlamizo.taskflow.api.model.Project;
import odunlamizo.taskflow.api.payload.request.ProjectPayload;

public interface ProjectService {

    Project addProject(ProjectPayload payload);

    Float retrieveProgress(String project);

    Project updateProject(long id, String deadline);

    Project deleteProject(long id);
    
}
