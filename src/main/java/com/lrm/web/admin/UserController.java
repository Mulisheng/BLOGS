package com.lrm.web.admin;

import com.lrm.dao.UserRepository;
import com.lrm.po.User;
import com.lrm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String userLoginPage(HttpSession session){

        if (session.getAttribute("flag").equals(1) && session.getAttribute("user") != null && session.getAttribute("user") != "")
        {
            return "redirect:/user/main";
        }else {


            return "admin/userlogin";
        }
    }

    @GetMapping("/userinfo/{id}")
    public String userInfo(@PathVariable Long id,Model model){
        System.out.println("gudegudegude");
        System.out.println("id"+ id);
        User user=userRepository.getOne(id);
        model.addAttribute("user",user);
        return "redirect:/user/main";
    }
//    @GetMapping("/test")
//    public String test()
//    {
//        return "/admin/userinfo";
//    }
public String encrytion(String s){
    char cs[]=s.toCharArray();
    for (int i=0;i<cs.length;i++) {
        cs[i] = (char) (cs[i] - 15);
    }
    s=new String(cs);
    String srevers=new StringBuffer(s).reverse().toString();
    return srevers;
}

    @PostMapping("/login")
    public String login(@RequestParam String username,
                    @RequestParam String password,
                    HttpSession session,
                    RedirectAttributes attributes) {
        //解密
        password=encrytion(password);
    User user = userService.checkUser(username, password);

    if (user != null) {
        if ( user.getType().equals(1)){
            //上错位置了
            attributes.addFlashAttribute("message", "用户名和密码错误");
            System.out.println("else");

            return "redirect:/user";

        }
        System.out.println("起飞");
        System.out.println(username);
        System.out.println(password);


        user.setPassword(null);

        session.setAttribute("user",user);
        session.setAttribute("flag",1);
        System.out.println("跳转了");

        return "/";
    } else {
        attributes.addFlashAttribute("message", "用户名和密码错误");
        System.out.println("else");

        return "redirect:/user";
    }
}


    @PostMapping("/useredit/{id}")
    public String userEdit(@PathVariable Long id,User user,RedirectAttributes attributes,HttpSession session)
    {

        System.out.println("id" + id);
        System.out.println(" 进/useredit/{id} ");
        System.out.println(user);

        User user1=userRepository.findOne(id);
        System.out.println(user1);
        SimpleDateFormat time=new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        Date date=new Date();
        System.out.println("当前时间:"+time.format(date));

        user1.setNickname(user.getNickname());
        user1.setEmail(user.getEmail());
        user1.setUsername(user.getUsername());
        user1.setUpdateTime(date);
        System.out.println(user1);
        userRepository.save(user1);
        session.setAttribute("user",userRepository.findOne(id));
        attributes.addFlashAttribute("message","修改信息成功");
        return "redirect:/admin/blogs";
    }


    @GetMapping("/updatepassword/{id}")
    public String editPasswd()
    {
//        User user=userRepository.getOne(id);
//        model.addAttribute("user",user);
//
        return "admin/updatepwd";
    }

    @PostMapping("/updatepassword/{id}")
    public String updatePassword(@PathVariable Long id,
                                 @RequestParam(name = "password") String pwd,
                                 @RequestParam(name = "new_password") String npwd,
                                 @RequestParam(name = "check_password") String cpwd,
                                 RedirectAttributes attributes,
                                 HttpSession session){

        String oldPwd=encrytion(pwd);
        String newPwd=encrytion(npwd);
        System.out.println("/updatepassword/{id}");
        System.out.println("id"+id);
        System.out.println("pwd"+pwd);

        User user=userRepository.getOne(id);

        if (user.getPassword().equals(oldPwd))
        {
            System.out.println("equals");

            if (npwd != null && npwd != "" && npwd.equals(cpwd))
            {
                System.out.println(" 2 equals");
                user.setPassword(newPwd);
                userRepository.save(user);
                attributes.addFlashAttribute("message","密码修改成功,请重新登陆");
                session.removeAttribute("user");
                return "redirect:/admin" ;
            }
            else
            {
                System.out.println(" 2 not equals");
                attributes.addFlashAttribute("message","修改失败，密码不一致");
                return "redirect:/user/updatepassword/{id}";
            }
        }else {
            System.out.println("not equals");
            attributes.addFlashAttribute("message","原密码不一致");
            return "redirect:/user/updatepassword/{id}";

        }

    }

    @GetMapping("/main")
    public String usermain()
    {
        System.out.println("/user/main");
        return "admin/usermain";
    }
}
