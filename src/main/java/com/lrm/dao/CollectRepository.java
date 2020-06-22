package com.lrm.dao;

import com.lrm.po.Biao;
import com.lrm.po.Collect;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectRepository extends JpaRepository<Collect,Long> {
    Collect findCollectByBlogidAndUserid(Long blogid, Long userid);

}
