package net.study.resume.entity;

import lombok.Data;
import net.study.resume.annotation.constraints.EnglishLanguage;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;


@Embeddable
@Access(AccessType.FIELD)
@Data
public class Contacts {

	
	@Column(length = 80)
	@EnglishLanguage
	@Size(max = 80,message = "skype name cannot be longer than 80 characters")
	@SafeHtml(message = "html tags not allowed")
	private String skype;

	@Column(length = 80)
	@EnglishLanguage
	@Size(max = 80,message = "link cannot be longer than 255 characters")
	@SafeHtml(message = "html tags not allowed")
	private String telegram;

	@Column
	@EnglishLanguage
	@Size(max = 255,message = "link cannot be longer than 255 characters")
	@URL(host="facebook.com",message = "wrong url format")
	private String facebook;

	@Column
	@EnglishLanguage
	@Size(max = 255,message = "link cannot be longer than 255 characters")
	@URL(host="linkedin.com",message = "wrong url format")
	private String linkedin;

	@Column
	@EnglishLanguage
	@Size(max = 255,message = "link cannot be longer than 255 characters")
	@URL(host="github.com",message = "wrong url format")
	private String github;
	
	@Column
	@EnglishLanguage
	@Size(max = 255,message = "link cannot be longer than 255 characters")
	@URL(host="stackoverflow.com",message = "wrong url format")
	private String stackoverflow;

	public Contacts() {
	}


	public Contacts(Contacts contacts) {
		this.skype = contacts.skype;
		this.telegram = contacts.telegram;
		this.facebook = contacts.facebook;
		this.linkedin = contacts.linkedin;
		this.github = contacts.github;
		this.stackoverflow = contacts.stackoverflow;
	}
}
