package com.lrm.service;

import com.lrm.dao.BlogRepository;
import com.lrm.dao.HistoryRepository;
import com.lrm.po.History;
import com.lrm.po.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
@Transactional
@Service
public class HistoryServiceImpl implements  HistoryService{
    @Autowired
    private HistoryRepository historyRepository;


    @Override
    public Page<History> listHistory(Pageable pageable, History history) {
        System.out.println("listHistoty in");
        return historyRepository.findAll(new Specification<History>() {
            @Override
            public Predicate toPredicate(javax.persistence.criteria.Root<History> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.equal(root.<Long>get("userid"),history.getUserid()));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    public History check(Long userid, Long blogid) {
        System.out.println("check!!");
       History history=historyRepository.findByUseridAndBlogid(userid,blogid);
        return history;
    }

    @Override
    public void delete(Long id) {
        historyRepository.delete(id);
    }


}
