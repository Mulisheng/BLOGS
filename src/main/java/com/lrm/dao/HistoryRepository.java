package com.lrm.dao;

import com.lrm.po.Blog;
import com.lrm.po.Comment;
import com.lrm.po.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History,Long>, JpaSpecificationExecutor<History> {
//    public History findByUseridAndBlogid(Long userid, Long blogid);
    History findByUseridAndBlogid(Long userid, Long  blogid);

    @Transactional
    @Modifying
    @Query(value = "delete from history where id order by id limit 1", nativeQuery = true)
    void deleteByQueryQuery();



}
