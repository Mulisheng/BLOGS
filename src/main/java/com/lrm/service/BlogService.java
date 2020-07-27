package com.lrm.service;

import com.lrm.po.Blog;
import com.lrm.po.User;
import com.lrm.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;


public interface BlogService {

    Blog getBlog(Long id);

    Blog getAndConvert(Long id);

//    ②全部获取
    Page<Blog> listBlog(Pageable pageable,BlogQuery blog);

    Page<Blog> listBlog3(Pageable pageable,BlogQuery blog,Long id);

    Page<Blog> listPost(Pageable pageable,Long userid);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlog(Long tagId,Pageable pageable);

    Page<Blog> listBlog(String query,Pageable pageable);

    List<Blog> listRecommendBlogTop(Integer size);

    Map<String,List<Blog>> archiveBlog();

    Long countBlog();

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id,Blog blog);

    void deleteBlog(Long id);

    public Page<Blog> listBlog2(Pageable pageable, BlogQuery blog);

    Page<Blog> tagBlogList(Pageable pageable,Blog blog,Long id);

    Page<Blog> typeBlogList(Pageable pageable,Long id);
    Page<Blog> idexTagBlogList(Pageable pageable,Long tagsId);




}
