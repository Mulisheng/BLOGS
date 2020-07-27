package com.lrm.service;

import com.lrm.dao.CollectRepository;
import com.lrm.po.Collect;
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
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class CollectServiceImpl implements CollectService{
    @Autowired
    CollectRepository collectRepository;

    @Override
    public void delectcollect(Long blogid, Long userid) {
        collectRepository.deleteCollectByBlogidAndUserid(blogid, userid);
    }

    @Override
    public Collect checkCollect(Long blogid, Long userid) {
        return collectRepository.findCollectByBlogidAndUserid(blogid, userid);
    }

    @Override
    public Page<Collect> listCollection(Pageable pageable, Long id) {
        System.out.println("listCollection in:");
        return collectRepository.findAll(new Specification<Collect>() {
            @Override
            public Predicate toPredicate(Root<Collect> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                System.out.println("toPredicate:");
                List<Predicate> predicates=new ArrayList<>();
                predicates.add(cb.equal(root.<Long>get("userid"),id));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    public Page<Collect> listCollection2(Pageable pageable, Collect collect,Long id) {
        return collectRepository.findAll(new Specification<Collect>() {
            @Override
            public Predicate toPredicate(Root<Collect> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
              //  System.out.println("toPredicate: list 2"+collect);
                List<Predicate> predicates=new ArrayList<>();
                if (!"".equals(collect.getTitle()) && collect.getTitle() != null) {
                    predicates.add(cb.like(root.<String>get("title"), "%"+collect.getTitle()+"%"));
                }
                predicates.add(cb.equal(root.<Long>get("userid"),id));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);

    }
}
