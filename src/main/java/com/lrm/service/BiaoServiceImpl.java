package com.lrm.service;

import com.lrm.dao.BiaoRepository;
import com.lrm.po.Biao;
import com.lrm.po.Blog;
import com.lrm.po.Type;
import com.lrm.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class BiaoServiceImpl implements BiaoService{
    @Autowired
    BiaoRepository biaoRepository;

    @Transactional
    @Override
    public String bsave(Biao biao){
        biaoRepository.save(biao);
        return "";
    }

    @Override
    public Page<Biao> listReport(Pageable pageable, Biao biao) {
        return biaoRepository.findAll(new Specification<Biao>() {
            @Override
            public Predicate toPredicate(Root<Biao> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(biao.getType()) && biao.getType() != null) {
                    predicates.add(cb.like(root.<String>get("type"), "%"+biao.getType()+"%"));
                }
//                predicates.add(cb.equal(root.<User>get("user").get("id"),id));
//                System.out.println(biao.isStatus());
//                predicates.add(cb.equal(root.<Boolean>get("status"),biao.isStatus()));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }
}
