package net.study.resume.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import net.study.resume.annotation.constraints.Adulthood;
import net.study.resume.annotation.constraints.EnglishLanguage;
import net.study.resume.annotation.constraints.NoStrings;
import net.study.resume.annotation.constraints.Phone;
import org.hibernate.validator.constraints.SafeHtml;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "profile")
@Document(indexName="profile")
@Data
public class Profile {
	@Id
	@SequenceGenerator(name = "PROFILE_ID_GENERATOR", sequenceName = "PROFILE_SEQ", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROFILE_ID_GENERATOR")
	@Column(unique = true, nullable = false)
	private Long id;

	@Column(name = "birth_day")
	@DateTimeFormat(iso= DateTimeFormat.ISO.DATE)
	@Adulthood(message = "you must be adult to proceed")
	private Date birthDay;

	@Column
	@Size(max=100)
	@EnglishLanguage(withNumbers=false, withSpechSymbols=false,message = "only latin characters are allowed")
	@SafeHtml(message = "html tags not allowed")
	private String city;

	@Column
	@Size(max=60)
	@EnglishLanguage(withNumbers=false, withSpechSymbols=false,message = "only latin characters are allowed")
	@SafeHtml(message = "html tags not allowed")
	private String country;

	@Column(name = "first_name", nullable = false, length = 50)
	@SafeHtml(message = "html tags not allowed")
	@Size(max = 50)
	private String firstName;

	@Column(name = "last_name", nullable = false, length = 50)
	@SafeHtml(message = "html tags not allowed")
	@Size(max = 50)
	private String lastName;

	@Column(length = 2147483647)
	@SafeHtml(message = "html tags not allowed")
	@Size
	private String objective;

	@Column(name = "large_photo")
	@Size(max=255)
	@org.springframework.data.annotation.Transient
	private String largePhoto;

	@Column(name = "small_photo")
	@Size(max=255)
	private String smallPhoto;

	@Column(length = 20)
	@Size(max=20)
	@Phone(message = "phone should be written in the international format")
	@org.springframework.data.annotation.Transient
	private String phone;

	@Column(length = 100)
	@Size(max=100)
	@Email
	@EnglishLanguage
	@org.springframework.data.annotation.Transient
	private String email;
	
	@Column
	@SafeHtml(message = "html tags not allowed")
	@Size
	private String info;

	@Column(length = 2147483647)
	@SafeHtml(message = "html tags not allowed")
	@Size
	private String summary;

	@Column(nullable = false, length = 100)
	@EnglishLanguage(withPunctuations = false,withSpechSymbols = false,withUidSymbols = true)
	@Size(min = 5,max=100)
    @NoStrings(value = {" "},message = "uid shouldn't contain spaces")
	@Field(type = FieldType.Keyword)
	private String uid;
	
	@Column(nullable = false, length = 100)
	@org.springframework.data.annotation.Transient
	private String password;
	
	@Column(nullable = false)
	@org.springframework.data.annotation.Transient
	private boolean completed;

	@Column(nullable = false)
	@org.springframework.data.annotation.Transient
	private boolean enabled;

	@Column(insertable=false)
	@Temporal(TemporalType.TIMESTAMP)
	@org.springframework.data.annotation.Transient
	private Date created;

	@OneToMany(mappedBy = "profile",cascade = CascadeType.ALL/*, cascade={CascadeType.MERGE, CascadeType.PERSIST}*/)
	@OrderBy("id ASC") //finishYear DESC, beginYear DESC,
	@org.springframework.data.annotation.Transient
	private List<Education> educations;

	@OneToMany(mappedBy = "profile",cascade = CascadeType.ALL/*, cascade={CascadeType.MERGE, CascadeType.PERSIST}*/)
	private List<Certificate> certificates;

	@OneToMany(mappedBy = "profile",cascade = CascadeType.ALL/*, cascade={CascadeType.MERGE, CascadeType.PERSIST}*/)
	@OrderBy("id ASC")
	@org.springframework.data.annotation.Transient
	private List<Hobby> hobbies;

	@OneToMany(mappedBy = "profile",cascade = CascadeType.ALL/*, cascade={CascadeType.MERGE, CascadeType.PERSIST}*/)
	@OrderBy("id ASC")
	private List<Language> languages;

	@OneToMany(mappedBy = "profile",cascade = CascadeType.ALL/*, cascade={CascadeType.MERGE, CascadeType.PERSIST}*/)
	@OrderBy("id ASC")
	private List<Practic> practics;

	@OneToMany(mappedBy = "profile",cascade = CascadeType.ALL/*, cascade={CascadeType.MERGE, CascadeType.PERSIST}*/)
	@OrderBy("id ASC")
	private List<Skill> skills;
	
	@OneToMany(mappedBy = "profile",cascade = CascadeType.ALL)
	@OrderBy("id ASC")
	private List<Course> courses;
	
	@JsonIgnore
	@Embedded
	private Contacts contacts;

	@Transient
	public String getFullName() {

		if(firstName!=null && lastName!=null){
			return firstName + " " + lastName;
		}
		else{
			return null;
		}
	}

	@Transient
	public int getAge(){
		LocalDate birthdate = new LocalDate (birthDay);
		LocalDate now = new LocalDate();
		Years age = Years.yearsBetween(birthdate, now);
		return age.getYears();
	}

	@Transient
	public String getProfilePhoto(){
		if(largePhoto != null) {
			return largePhoto;
		} else {
			return "/static/img/profile-placeholder.png";
		}
	}

	public Contacts getContacts() {
		if(contacts == null) {
			contacts = new Contacts();
		}
		return contacts;
	}

	public String getSmallPhoto(){
		if(smallPhoto != null) {
			return smallPhoto;
		} else {
			return "/static/img/profile-placeholder.png";
		}
	}

	public String getLargePhoto(){
		if(largePhoto != null) {
			return largePhoto;
		} else {
			return "/static/img/profile-placeholder.png";
		}
	}

}