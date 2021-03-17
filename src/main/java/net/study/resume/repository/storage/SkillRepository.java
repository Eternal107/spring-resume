package net.study.resume.repository.storage;

import net.study.resume.annotation.RepositoryEntityClass;
import net.study.resume.entity.Profile;
import net.study.resume.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillRepository extends AbstractProfileRepository<Skill> {

    List<Skill> findByProfileId(Long id);


}
