package net.study.resume.repository.storage;

import net.study.resume.entity.Skill;
import net.study.resume.entity.SkillCategory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillCategoryRepository extends JpaRepository<SkillCategory,Short> {

    List<SkillCategory> findAll(Sort sort);

}
