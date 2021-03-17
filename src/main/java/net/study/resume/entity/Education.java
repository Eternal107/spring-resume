package net.study.resume.entity;

import lombok.Data;
import net.study.resume.annotation.constraints.EnglishLanguage;
import net.study.resume.annotation.constraints.FirstFieldLessThanSecond;
import org.hibernate.validator.constraints.SafeHtml;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name = "education")
@FirstFieldLessThanSecond(first = "beginYear", second = "finishYear")
@Data
public class Education implements ProfileEntity {


	@Id
	@SequenceGenerator(name = "EDUCATION_ID_GENERATOR", sequenceName = "EDUCATION_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EDUCATION_ID_GENERATOR")
	@Column(unique = true, nullable = false)
	private Long id;

	@Column(nullable = false)
	@SafeHtml(message = "html tags not allowed")
	@EnglishLanguage(withSpechSymbols = false)
	private String faculty;

	@Column(nullable = false, length = 100)
	@SafeHtml(message = "html tags not allowed")
	@EnglishLanguage(withSpechSymbols = false)
	private String summary;

	@Column(nullable = false, length = 2147483647)
	@SafeHtml(message = "html tags not allowed")
	@EnglishLanguage(withSpechSymbols = false)
	@Size
	private String university;

	@Column(name = "begin_year", nullable = false)
	private Integer beginYear;

	@Column(name = "finish_year")
	private Integer finishYear;

	// bi-directional many-to-one association to Profile
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_profile", nullable = false)
	@org.springframework.data.annotation.Transient
	private Profile profile;

}