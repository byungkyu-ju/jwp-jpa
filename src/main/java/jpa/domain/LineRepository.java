package jpa.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : byungkyu
 * @date : 2020/12/17
 * @description :
 **/
public interface LineRepository extends JpaRepository<Line, Long> {
	Line findByName(String name);
}
