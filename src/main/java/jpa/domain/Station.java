package jpa.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * @author : byungkyu
 * @date : 2020/12/17
 * @description :
 **/
@Entity
public class Station {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@ManyToOne
	@JoinColumn(name = "line_id") //station이 관리할 line의 이름
	private Line line;

	public Station() {

	}

	public Station(final Long id, final String name) {
		this.id = id;
		this.name = name;
	}

	public Station(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void changeName(final String name) {
		this.name = name;
	}

	public void setLine(Line line) {
		this.line = line;
	}

	public Line getLine() {
		return line;
	}

}
