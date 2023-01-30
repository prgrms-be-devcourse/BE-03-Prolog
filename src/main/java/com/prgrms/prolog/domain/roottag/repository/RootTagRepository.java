package com.prgrms.prolog.domain.roottag.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prgrms.prolog.domain.roottag.model.RootTag;

public interface RootTagRepository extends JpaRepository<RootTag, Long> {

	@Query("""
		SELECT rt
		FROM RootTag rt
		WHERE rt.name IN :tagNames
		""")
	Set<RootTag> findByTagNamesIn(@Param(value = "tagNames") Set<String> tagNames);
}
