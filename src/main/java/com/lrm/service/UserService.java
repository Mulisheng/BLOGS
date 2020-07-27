package com.lrm.service;

import com.lrm.po.Biao;
import com.lrm.po.Blog;
import com.lrm.po.User;
import com.lrm.vo.UserQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpSession;


public interface UserService {
    Page<User> listUser1(Pageable pageable);
    User updateUser(Long id);
    Page<User> listUser(Pageable pageable, UserQuery userQuery,HttpSession session);
    Page<User> listUserSearch(Pageable pageable,User user);
    User checkUser(String username, String password);
    User checkUserName(String username);
    void toMail(String to,String userName,Long status,User user);
    void findPwd(String userName,String email,String pwd);
    void sendReportMail(Long id, Biao biao,Boolean ban);
}
