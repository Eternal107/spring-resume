package net.study.resume.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import net.study.resume.annotation.constraints.EnglishLanguage;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="certificate")
@Data
public class Certificate implements ProfileEntity {

	@Id
	@SequenceGenerator(name="CERTIFICATE_ID_GENERATOR", sequenceName="CERTIFICATE_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CERTIFICATE_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Long id;

	@Column(name="large_url", nullable=false)
	@Size(max = 255)
	@org.springframework.data.annotation.Transient
	@NotNull
	private String largeUrl;
	
	@Column(name="small_url", nullable=false)
	@Size(max = 255)
	@org.springframework.data.annotation.Transient
	@NotNull
	private String smallUrl;

	@Column(nullable=false, length=50)
	@EnglishLanguage
	@SafeHtml(message = "html tags not allowed")
	@Size(max = 50)
	private String name;

	//bi-directional many-to-one association to Profile
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_profile", nullable=false)
	@org.springframework.data.annotation.Transient
	private Profile profile;
	

}