package com.lrm.service;

import com.lrm.NotFoundException;
import com.lrm.dao.BlogRepository;
import com.lrm.dao.UserRepository;
import com.lrm.po.Biao;
import com.lrm.po.Blog;
import com.lrm.po.Type;
import com.lrm.po.User;
import com.lrm.util.MD5Utils;
import com.lrm.util.MyBeanUtils;
import com.lrm.vo.BlogQuery;
import com.lrm.vo.UserQuery;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {



    private String muserName="1119322361@qq.com";
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

//    listUser1

    //③多条件查询   返回 Page<>
    @Override
    public Page<User> listUser1(Pageable pageable) {
        return userRepository.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }


    @Transactional
    @Override
    public User updateUser(Long id) {
        User user = userRepository.findOne(id);
        Long status;
        if (user== null) {
            throw new NotFoundException("该用户不存在");
        }
        if(user.getStatus().equals(new Long(1))){
            status=new Long(9999);
        }else{
            status=new Long(1);
        }

        user.setStatus(status);
        System.out.println("ready to save");
        return userRepository.save(user);
    }


    //③多条件查询   返回 Page<>
    @Override
    public Page<User> listUser(Pageable pageable, UserQuery userQuery, HttpSession session) {
        return userRepository.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (userQuery.getId() != null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), userQuery.getId()));
                }
                if (userQuery.getId() != null) {
                    User user1=(User)session.getAttribute("user");
                    predicates.add(cb.notEqual(root.<Type>get("type").get("id"),user1.getId()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    public Page<User> listUserSearch(Pageable pageable, User user) {
        return userRepository.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (user.getStatus() != null && !user.getStatus().equals("")) {
                    predicates.add(cb.equal(root.<Long>get("status"), user.getStatus()));
                }
                if (user.getUsername()!=null && !user.getUsername().equals("")) {
                 //   User user1=(User)session.getAttribute("user");
                    predicates.add(cb.like(root.<String>get("username"),"%"+user.getUsername()+"%"));
                }
                predicates.add(cb.notEqual(root.<Integer>get("type"),1));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    public User checkUser(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username,password);
        return user;
    }

    @Override
    public User checkUserName(String username) {
        User user=userRepository.findUserByUsername(username);
        return user;
    }

    public void toMail(String to,String userName,Long status,User user)  {
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        System.out.println(muserName);
        simpleMailMessage.setFrom(muserName);
        String subject="航院论坛";
        String text="";

        if (status.equals(new Long(1))) {
            blogRepository.updateunban(user.getId());
            text = "尊敬的"+    userName    +"用户你好,你的账户已经解除冻结";
        }
        else if (status.equals(new Long(9999)))
        {
            blogRepository.updateban(user.getId());
            text = "尊敬的"+  userName   +"用户你好,你的账户已冻结";
        }
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);
        javaMailSender.send(simpleMailMessage);
    }




    public void sendReportMail(Long id,Biao biao,Boolean ban)  {
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        System.out.println(muserName);
        simpleMailMessage.setFrom(muserName);
        String text="";
        String userName="";
        String to="";
        String subject="航院论坛";
        if (ban==false)
        {
            userName = biao.getUser().getUsername();
            to = biao.getUser().getEmail();
            text = "尊敬的" + userName + "您好,你举报的帖子并不存在违规现象，如有问题请联系管理员 管理员QQ: 1119322361";

        }
        else {
            if (biao.getUser().getId() == id) {
                userName = biao.getUser().getUsername();
                to = biao.getUser().getEmail();
                text = "尊敬的" + userName + "您好,你举报的帖子存在违规现象已被管理员处理，如有问题请联系管理员 管理员QQ: 1119322361";
            } else {
                Blog blog = blogRepository.findOne(id);
                userName = blog.getUser().getUsername();
                to = blog.getUser().getEmail();
                text = "尊敬的" + userName + "您好,你的帖子存在违规现象，所以管理员要对您的帖子封禁，如有问题请联系管理员 管理员QQ: 1119322361";
            }
        }
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);
        javaMailSender.send(simpleMailMessage);
    }






    public void findPwd(String userName,String to,String pwd)  {
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        System.out.println(muserName);
        simpleMailMessage.setFrom(muserName);
        String subject="航院论坛";
        String text="尊敬的"+  userName  +"您的密码为"+   pwd    +"";

        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);

        javaMailSender.send(simpleMailMessage);




    }


}
