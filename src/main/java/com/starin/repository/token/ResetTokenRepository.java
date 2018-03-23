package com.starin.repository.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.starin.domain.ResetToken;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, Integer> {
	ResetToken findByToken(String token);

	@Modifying
	@Query("UPDATE ResetToken r set r.deleted=true where r.deleted=false and r.user.uid=:uid")
	void deleteOldTokens(@Param("uid") int uid);
}
