package net.study.resume.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import net.study.resume.annotation.constraints.EnglishLanguage;
import net.study.resume.model.LanguageLevel;
import net.study.resume.model.LanguageType;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import javax.validation.constraints.Size;


@Entity
@Table(name="language")
@Data
public class Language implements ProfileEntity {


	@Id
	@SequenceGenerator(name="LANGUAGE_ID_GENERATOR", sequenceName="LANGUAGE_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LANGUAGE_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Long id;

	@Column(nullable=false)
	@org.springframework.data.annotation.Transient
	@Enumerated(EnumType.STRING)
	private LanguageLevel level;

	@Column(nullable=false, length=30)
	@EnglishLanguage(withSpechSymbols=false, withNumbers=false, withPunctuations=false)
	@SafeHtml(message = "html tags not allowed")
	@Size(max = 30)
	private String name;
	
	@Column
	@Enumerated(EnumType.STRING)
	private LanguageType type;

	//bi-directional many-to-one association to Profile
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_profile", nullable=false)
	@org.springframework.data.annotation.Transient
	private Profile profile;

	@Transient
	public boolean isHasLanguageType(){
		return type != LanguageType.ALL;
	}


}