package com.lrm.service;

import com.lrm.po.Following;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FollowService {
    Following save(Following following);
    List<Following> find(Long id);
    Following checkFollowing(Long userId,Long followId);
    void deleteFollowing(Long id);
    Page<Following> listFollowing(Pageable pageable,Long id);
    Page<Following> listFollowing2(Pageable pageable,Long id,Following following);
}
