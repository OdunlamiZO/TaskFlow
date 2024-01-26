package odunlamizo.taskflow.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import odunlamizo.taskflow.auth.model.ProfilePhoto;

@Transactional(readOnly = true)
public interface ProfilePhotoRepository extends JpaRepository<ProfilePhoto, Integer> {

    Optional<ProfilePhoto> findByUser(String user);

}
