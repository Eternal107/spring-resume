package net.study.resume.service;

import net.study.resume.entity.SkillCategory;
import net.study.resume.repository.storage.SkillCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillCategoryService {

    @Autowired
    SkillCategoryRepository skillCategoryRepository;

    public List<SkillCategory> findAll(Sort sort){
        return skillCategoryRepository.findAll(sort);
    }
}
