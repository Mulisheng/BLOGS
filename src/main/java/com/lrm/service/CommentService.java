package com.lrm.service;

import com.lrm.po.Comment;

import javax.servlet.http.HttpSession;
import java.util.List;


public interface CommentService {

    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment, HttpSession session);
}
