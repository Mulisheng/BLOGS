package com.lrm.dao;

import com.lrm.po.Blog;
import com.lrm.po.User;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface UserRepository extends JpaRepository<User,Long> , JpaSpecificationExecutor<User> {
    User findUserByUsername(String username);
    User findByUsernameAndPassword(String username, String password);
    User findUserByEmail(String email);


//    findBy   Username  And   Password

}
