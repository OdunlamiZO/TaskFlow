package odunlamizo.taskflow.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import odunlamizo.taskflow.auth.model.File;

@Transactional(readOnly = true)
public interface FileRepository extends JpaRepository<File, Long> {

}
