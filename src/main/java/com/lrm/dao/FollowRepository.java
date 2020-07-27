package com.lrm.dao;

import com.lrm.po.Following;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface FollowRepository extends JpaRepository<Following,Long>, JpaSpecificationExecutor<Following> {
    List<Following> findFollowingByUserId(Long userId);
    Following findFollowingByUserIdAndFollowingId(Long userId,Long FollowingId);

}
