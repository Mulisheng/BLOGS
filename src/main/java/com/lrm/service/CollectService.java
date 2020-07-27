package com.lrm.service;

import com.lrm.po.Collect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CollectService {
    Collect checkCollect(Long blogid, Long userid);
    Page<Collect> listCollection(Pageable pageable, Long id);
    Page<Collect> listCollection2(Pageable pageable, Collect collect,Long id);
    void delectcollect(Long blogid, Long userid);
}
