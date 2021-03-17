package net.study.resume.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="hobby")
@Data
public class Hobby implements ProfileEntity {


	@Id
	@SequenceGenerator(name="HOBBY_ID_GENERATOR", sequenceName="HOBBY_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HOBBY_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Long id;

	@Column(nullable=false, length=30)
	private String name;

	//bi-directional many-to-one association to Profile
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_profile", nullable=false)
	@org.springframework.data.annotation.Transient
	private Profile profile;
	
	@Transient
	private boolean selected;

	@Transient
	public String getCssClassName(){
		return name.replace(" ", "-").toLowerCase();
	}

	public Hobby() {
	}

	public Hobby(String name) {
		this.name = name;
	}

	public Hobby(String name,boolean selected) {
		this.name = name;
		this.selected = selected;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Hobby)) return false;
		Hobby hobby = (Hobby) o;
		return Objects.equals(getName(), hobby.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName());
	}
}