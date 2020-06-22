package com.lrm.web;

import com.lrm.po.Biao;
import com.lrm.service.BiaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/dct")
public class DctController {
    @Autowired
    private BiaoService biaoService;

    @PostMapping("/dct1/{id}")
    public String dct1(@PathVariable Long id, Model model, HttpServletRequest httpServletRequest, HttpSession session){
        System.out.println(httpServletRequest.getRequestURL().toString());

        session.setAttribute("url",httpServletRequest.getRequestURL());
        System.out.println(session.getAttribute("url"));
        System.out.println("/DCT1");
        model.addAttribute("blogId",id);
        System.out.println(id);

        return "report";
    }

    @RequestMapping("/dct2")
    public String dct2(Biao biao, @RequestParam String dt, Model model, @RequestParam(name = "blogid") Long id
            , HttpSession session, @RequestParam(name = "type") String type, RedirectAttributes attributes){


        System.out.println(type);
        System.out.println(id);
        System.out.println(session.getAttribute("url"));
        attributes.addFlashAttribute("message","举报成功");
        biao.setUrl(session.getAttribute("url").toString());
        biao.setDct(dt);
        biao.setBlogid(id);
        biao.setType(type);
        biaoService.bsave(biao);
        return "redirect:/";
    }

}
