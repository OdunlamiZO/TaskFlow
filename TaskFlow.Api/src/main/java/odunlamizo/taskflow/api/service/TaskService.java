package odunlamizo.taskflow.api.service;

import odunlamizo.taskflow.api.model.Task;
import odunlamizo.taskflow.api.payload.request.TaskPayload;
import odunlamizo.taskflow.api.payload.request.TaskUpdatePayload;

public interface TaskService {

    Task addTask(TaskPayload payload);

    Task updateTask(long id, TaskUpdatePayload payload);

    Task deleteTask(long id);
    
}
