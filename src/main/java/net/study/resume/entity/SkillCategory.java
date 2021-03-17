package net.study.resume.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name="skill_category")
@Data
public class SkillCategory {

	@Id
	@Column
	private Short id;

	@Column(nullable=false, length=50)
	private String category;

	@OneToMany(mappedBy = "skillCategory")
	@org.springframework.data.annotation.Transient
	private List<Skill> skills;

}
