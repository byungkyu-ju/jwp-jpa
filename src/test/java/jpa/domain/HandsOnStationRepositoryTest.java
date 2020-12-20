package jpa.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * @author : byungkyu
 * @date : 2020/12/17
 * @description : 핸즈온시 작성
 **/
@DataJpaTest
class HandsOnStationRepositoryTest {
	@Autowired
	private StationRepository stations;

	@Autowired
	private LineRepository lines;

	@Test
	void save() {
		final Station expected = new Station("잠실역");
		assertThat(expected.getId()).isNull();
		final Station actual = stations.save(expected);
		assertThat(actual.getId()).isNotNull();
		assertThat(actual.getName()).isEqualTo("잠실역");
	}

	@Test
	void findByName() {
		stations.save(new Station("잠실역"));
		final Station actual = stations.findByName("잠실역");
		assertThat(actual.getName()).isEqualTo("잠실역");
	}

	//동일성 보장
	//id가 같다면 영속성컨텍스트가 같다는걸 보장해줌.
	//
	@Test
	void identity() {
		final Station station1 = stations.save(new Station("잠실역"));
		final Station station2 = stations.findById(station1.getId()).get();
		assertThat(station1 == station2).isTrue();
		assertThat(station1.getName().equals(station2.getName()));

		final Station station3 = stations.findByName("잠실역");
		assertThat(station1 == station3).isTrue();
	}

	//쓰기지연의 동작 테스트
	@Test
	void update() {
		final Station station1 = stations.save(new Station("잠실역")); //1.1차캐시로 만들고 스냅샷을 생성함
		station1.changeName("몽촌토성역"); //2. 1차캐시의 스냅샷과 비교하여 변경이 일어나면

		// 영속성 컨텍스트에서 name으로는 엔티티 클래스를 찾을 수 없다
		// DB와 어긋남을 방지하려면, DB와 바로 찌르기 전에 flush를 함.
		//findByName 같은 JPQL은 캐시에 물어보지 않고, 바로 DB로 물어봄.

		//1.findByName을 하면 1차캐시의 스냅샷과 기존 엔티티를 비교를 하게 되고

		final Station station2 = stations.findByName("몽촌토성역");
		assertThat(station2).isNotNull();
	}

	@Test
	void 지하철역이_어느_노선에_속했는지_알_수_있다() {
		final Line line = lines.save(new Line("2호선"));
		final Station station = new Station("잠실역");
		station.setLine(line);
		//persist
		final Station actual = stations.save(station);
		//stations.flush();

		final Station station2 = stations.findByName("잠실역");
		assertThat(station.getLine().getName()).isEqualTo("2호선");

	}

	private void saves() {
		final Line line = lines.save(new Line("2호선"));
		final Station station = new Station("교대역");
		station.setLine(line);
		stations.save(station);
		final Station actual = stations.save(station);
	}

	@Test
	void findByNameWithLine() {
		final Station station = stations.findByName("교대역");
		assertThat(station).isNotNull();
		assertThat(station.getLine().getName()).isEqualTo("3호선");
	}

	@Test
	void updateWithLine() {
		final Station station = stations.findByName("교대역");
		station.setLine(lines.save(new Line("2호선")));
		stations.flush();
	}

	@Test
	void removeWithLine() {
		final Station station = stations.findByName("교대역");
		station.setLine(null);
		stations.flush();
	}

	@Test
	void findById() {
		final Line line = lines.findByName("3호선");
		assertThat(line.getStations()).hasSize(1);
	}

	@Test
	void master() {
		final Line line = lines.findByName("3호선");
		assertThat(line.getStations()).hasSize(1);
		final Station station = new Station("양재역");
		stations.save(station); //영속상태로 만들어야함. 이 때 쿼리 실행
		line.getStations().add(station); //여기는 반응이 안됨. 왜냐면 연관관계의 주인이 아니기 때문에 조회만 됨.
		assertThat(line.getStations()).hasSize(2);
		stations.flush();

	}

	@Test
	void 연관관계_편의_메서드() {
		final Line line = lines.findByName("3호선");
		assertThat(line.getStations()).hasSize(1);
		final Station station = new Station("양재역");
		stations.save(station); //영속상태로 만들어야함. 이 때 쿼리 실행

		line.addStation(station);  // << 편의메서드를 확인해야함!! 순환참조가 발생할 수 있기 때문에 조심해야함.

		assertThat(line.getStations()).hasSize(2);
		stations.flush();

	}
}