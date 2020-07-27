package com.lrm.dao;

import com.lrm.po.Blog;
import com.lrm.po.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment,Long>{
    @Transactional
    @Modifying
    @Query(value="update t_comment set avatar=? where uid=?",nativeQuery = true)
    void updateavatar(String avatar,Long id);

    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);
}
