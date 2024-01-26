package odunlamizo.taskflow.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import odunlamizo.taskflow.api.model.Project;

@Transactional(readOnly = true)
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByOwner(String owner);
    
}
