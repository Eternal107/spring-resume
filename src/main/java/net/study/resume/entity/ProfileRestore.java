package net.study.resume.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "profile_restore")
@Data
public class ProfileRestore  {

	@Id
	@Column(unique = true, nullable = false)
	private Long id;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id", nullable=false)
	private Profile profile;

	@Column(nullable = false, unique = true)
	private String token;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date created;
	

}