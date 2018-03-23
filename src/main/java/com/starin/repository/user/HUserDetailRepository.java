package com.starin.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.starin.domain.user.HUserDetail;

@Repository
public interface HUserDetailRepository extends JpaRepository<HUserDetail, Integer> {

}
