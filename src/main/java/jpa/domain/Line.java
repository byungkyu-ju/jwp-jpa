package jpa.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * @author : byungkyu
 * @date : 2020/12/17
 * @description :
 **/
@Entity
public class Line {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;

	@OneToMany(mappedBy = "line")
	private List<Station> stations;

	public Line() {
	}

	public Line(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Station> getStations() {
		return stations;
	}

	public void addStation(final Station station) {
		stations.add(station);
		station.setLine(this); // 연관관계의 주인이 여기서 동작하게 됨
	}
}
