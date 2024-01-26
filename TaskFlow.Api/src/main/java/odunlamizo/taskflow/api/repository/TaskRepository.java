package odunlamizo.taskflow.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import odunlamizo.taskflow.api.model.Task;

@Transactional(readOnly = true)
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByOwner(String owner);

    @Query("select t from Task t where t.project = :project and t.owner = :owner")
    List<Task> findByProject(String project, String owner);

}
