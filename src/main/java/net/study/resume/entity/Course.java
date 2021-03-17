package net.study.resume.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import net.study.resume.annotation.constraints.EnglishLanguage;
import org.hibernate.validator.constraints.SafeHtml;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Date;



@Entity
@Table(name = "course")
@Data
public class Course implements ProfileEntity {

	@Id
	@SequenceGenerator(name = "COURSE_ID_GENERATOR", sequenceName = "COURSE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COURSE_ID_GENERATOR")
	@Column(unique = true, nullable = false)
	private Long id;

	@Column(length = 60)
	@EnglishLanguage(withSpechSymbols = false)
	private String name;

	@Column(length = 60)
	@EnglishLanguage(withSpechSymbols = false)
	@SafeHtml(message = "html tags not allowed")
	private String school;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_profile", nullable = false)
	@org.springframework.data.annotation.Transient
	private Profile profile;

	@Column(name = "finish_date", nullable = false)
	@Temporal(TemporalType.DATE)
	@org.springframework.data.annotation.Transient
	private Date finishDate;

	@Transient
	@org.springframework.data.annotation.Transient
	private Integer finishDateMonth;

	@Transient
	@org.springframework.data.annotation.Transient
	private Integer finishDateYear;

	@Transient
	public Integer getFinishDateMonth() {
		if (finishDate != null) {
			return new DateTime(finishDate).getMonthOfYear();
		} else {
			return null;
		}
	}

	@Transient
	public Integer getFinishDateYear() {
		if (finishDate != null) {
			return new DateTime(finishDate).getYear();
		} else {
			return null;
		}
	}

	public void setFinishDateMonth(Integer finishDateMonth) {
		this.finishDateMonth = finishDateMonth;
		setupFinishDate();
	}

	public void setFinishDateYear(Integer finishDateYear) {
		this.finishDateYear = finishDateYear;
		setupFinishDate();
	}

	private void setupFinishDate() {
		if (finishDateYear != null && finishDateMonth != null) {
			setFinishDate(new Date(new DateTime(finishDateYear, finishDateMonth, 1, 0, 0).getMillis()));
		} else {
			setFinishDate(null);
		}
	}

}
