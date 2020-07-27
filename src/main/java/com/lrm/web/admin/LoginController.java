package com.lrm.web.admin;

import com.lrm.dao.BiaoRepository;
import com.lrm.dao.UserRepository;
import com.lrm.po.Biao;
import com.lrm.po.User;
import com.lrm.service.BiaoService;
import com.lrm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * Created by limi on 2017/10/15.
 */
@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private BiaoService biaoService;
    private UserRepository userRepository;





    @GetMapping
    public String loginPage( HttpSession session) {
        //游客  设置的地方在/位置
//        if(session.getAttribute("flag").equals(0)){
//            return "admin/login";
//        }

        Object obj=session.getAttribute("user");
        User u1=(User)obj;
        System.out.println("来了来了真的来了");

        System.out.println("来了来了真的来了");
        if(!session.getAttribute("flag").equals(0) && session.getAttribute("user")!=null && session.getAttribute("user")!="" && u1.getType().equals(1) ){
            return "admin/index";
        }
        System.out.println("是的是的是的来了来了真的来了1111111111111111111111");
        return "admin/login";
    }

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
        System.out.println("进/login");
        password=encrytion(password);
        User user = userService.checkUser(username, password);

        if (user != null) {
            if ( user.getType().equals(2)) {
                //上错位置了
                attributes.addFlashAttribute("message", "用户名和密码错误");
                System.out.println("else");

                return "redirect:/admin";
            }
            System.out.println(username);
            System.out.println(password);

            Biao biao=new Biao();
           // user.setPassword(null);
            session.setAttribute("user",user);
            session.setAttribute("flag",2);
            System.out.println("马上跳转");
            session.setAttribute("flag1",2);

            return "redirect:/admin";
        } else {
            attributes.addFlashAttribute("message", "用户名和密码错误");
            System.out.println("else");

            return "redirect:/admin";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        session.setAttribute("flag",0);
        session.setAttribute("flag1",0);
        return "redirect:/";
    }



}
