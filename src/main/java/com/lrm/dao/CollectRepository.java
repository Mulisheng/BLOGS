package com.lrm.dao;

import com.lrm.po.Biao;
import com.lrm.po.Collect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CollectRepository extends JpaRepository<Collect,Long> , JpaSpecificationExecutor<Collect> {
    Collect findCollectByBlogidAndUserid(Long blogid, Long userid);
    void deleteCollectByBlogidAndUserid(Long blogid, Long userid);
}
