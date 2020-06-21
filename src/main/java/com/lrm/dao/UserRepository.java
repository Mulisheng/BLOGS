package com.lrm.dao;

import com.lrm.po.User;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface UserRepository extends JpaRepository<User,Long> {
    User findUserByUsername(String username);
    User findByUsernameAndPassword(String username, String password);


//    findBy   Username  And   Password

}
