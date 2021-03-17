package net.study.resume.repository.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface AbstractProfileRepository<T> extends JpaRepository<T, Long> {

    void deleteByProfileId(Long idProfile);

    List<T> findByProfileId(Long id);

}
