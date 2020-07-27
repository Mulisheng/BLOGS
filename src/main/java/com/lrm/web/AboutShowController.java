package com.lrm.web;

import com.lrm.po.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;


@Controller
public class AboutShowController {

    @GetMapping("/about")
    public String about() {


      //  System.out.println("进about+++"+user);
        return "about";
    }
}
