package com.lrm.service;

import com.lrm.dao.FollowRepository;
import com.lrm.po.Following;
import com.lrm.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class FollowServiceImpl implements FollowService{
    @Autowired
    private FollowRepository followRepository;

    @Override
    public Following save(Following following) {
        return followRepository.save(following);
    }

    @Override
    public List<Following> find (Long id) {
        return followRepository.findFollowingByUserId(id);
    }

    @Override
    public Following checkFollowing(Long userId, Long followId) {
        return followRepository.findFollowingByUserIdAndFollowingId(userId,followId);
    }

    @Override
    public void  deleteFollowing(Long id) {
         followRepository.delete(id);
    }

    @Override
    public Page<Following> listFollowing(Pageable pageable, Long id) {
        return followRepository.findAll(new Specification<Following>() {
            @Override
            public Predicate toPredicate(Root<Following> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates=new ArrayList<>();
                predicates.add(cb.equal(root.<User>get("user").get("id"),id));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Following> listFollowing2(Pageable pageable, Long id, Following following) {
        return followRepository.findAll(new Specification<Following>() {
            @Override
            public Predicate toPredicate(Root<Following> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(following.getFollowName()) && following.getFollowName() != null) {
                    predicates.add(cb.like(root.<String>get("followName"), "%" + following.getFollowName() + "%"));
                }
                predicates.add(cb.equal(root.<User>get("user").get("id"), id));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }
}
