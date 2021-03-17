package net.study.resume.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import net.study.resume.annotation.constraints.EnglishLanguage;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "skill")
@Data
public class Skill implements ProfileEntity {

	@Id
	@SequenceGenerator(name = "SKILL_ID_GENERATOR", sequenceName = "SKILL_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SKILL_ID_GENERATOR")
	@Column(unique = true, nullable = false)
	private Long id;

	@Column(nullable = false, length = 2147483647)
	@EnglishLanguage
	@SafeHtml(message = "html tags not allowed")
	@Size
	private String value;

	// bi-directional many-to-one association to Profile
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_profile", nullable = false)
	@org.springframework.data.annotation.Transient
	private Profile profile;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private SkillCategory skillCategory;


}