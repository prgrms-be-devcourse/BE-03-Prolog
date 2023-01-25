package com.prgrms.prolog.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.prgrms.prolog.domain.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("""
		SELECT u
		FROM User u
		WHERE u.provider = :provider
		and u.oauthId = :oauthId
		""")
	Optional<User> findByProviderAndOauthId(String provider, String oauthId);
}
