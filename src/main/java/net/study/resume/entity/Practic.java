package net.study.resume.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import net.study.resume.annotation.constraints.EnglishLanguage;
import net.study.resume.annotation.constraints.FirstFieldLessThanSecond;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;


@Entity
@Table(name = "practic")
@FirstFieldLessThanSecond.List({@FirstFieldLessThanSecond(first = "beginDate", second = "finishDate",message = "Begin date must be less than end date")})
@Data
public class Practic implements ProfileEntity  {


	@Id
	@SequenceGenerator(name = "PRACTIC_ID_GENERATOR", sequenceName = "PRACTIC_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRACTIC_ID_GENERATOR")
	@Column(unique = true, nullable = false)
	private Long id;

	@Column(nullable = false, length = 100)
	@EnglishLanguage(withSpechSymbols = false)
	@SafeHtml(message = "html tags not allowed")
	@Size(max = 100)
	private String company;

	@Column(length = 255)
	@org.springframework.data.annotation.Transient
	@EnglishLanguage
	@URL(message = "wrong url format")
	@Size(max = 255)
	private String demo;

	@Column(length = 255)
	@org.springframework.data.annotation.Transient
	@EnglishLanguage
	@URL(message = "wrong url format")
	@Size(max = 255)
	private String src;

	@Column(nullable = false, length = 100)
	@EnglishLanguage(withSpechSymbols = false)
	@SafeHtml(message = "html tags not allowed")
	@Size(max = 100)
	private String position;

	@Column(nullable = false, length = 2147483647)
	@EnglishLanguage(withSpechSymbols = false)
	@SafeHtml(message = "html tags not allowed")
	@Size
	private String responsibilities;

	@Column(name = "begin_date", nullable = false)
	@Temporal(TemporalType.DATE)
	@org.springframework.data.annotation.Transient
	private Date beginDate;

	@Transient
	@org.springframework.data.annotation.Transient
	private Integer beginDateMonth;

	@Transient
	@org.springframework.data.annotation.Transient
	private Integer beginDateYear;

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


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_profile", nullable = false)
	@org.springframework.data.annotation.Transient
	private Profile profile;


	@Transient
	public Integer getBeginDateMonth() {
		if (beginDate != null) {
			return new DateTime(beginDate).getMonthOfYear();
		} else {
			return null;
		}
	}

	@Transient
	public Integer getBeginDateYear() {
		if (beginDate != null) {
			return new DateTime(beginDate).getYear();
		} else {
			return null;
		}
	}

	public void setBeginDateMonth(Integer beginDateMonth) {
		this.beginDateMonth = beginDateMonth;
		setupBeginDate();
	}

	public void setBeginDateYear(Integer beginDateYear) {
		this.beginDateYear = beginDateYear;
		setupBeginDate();
	}

	private void setupBeginDate() {
		if (beginDateYear != null && beginDateMonth != null) {
			setBeginDate(new Date(new DateTime(beginDateYear, beginDateMonth, 1, 0, 0).getMillis()));
		} else {
			setBeginDate(null);
		}
	}

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