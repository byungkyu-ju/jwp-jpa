package jpa.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : byungkyu
 * @date : 2020/12/17
 * @description :
 **/
public interface StationRepository extends JpaRepository<Station, Long> {
	Station findByName(String name);
}
