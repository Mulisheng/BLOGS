package com.lrm.web;

import com.lrm.dao.BiaoRepository;
import com.lrm.dao.UserRepository;
import com.lrm.po.Biao;
import com.lrm.po.Blog;
import com.lrm.po.User;
import com.lrm.service.BiaoService;
import com.lrm.service.BlogService;
import com.lrm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/dct")
public class DctController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private BiaoService biaoService;
    @Autowired
    private BiaoRepository biaoRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
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

        Blog blog=blogService.getBlog(id);
        User u=(User) session.getAttribute("user");
        System.out.println(session.getAttribute("url"));
        attributes.addFlashAttribute("message","举报成功");
        if (type.equals("") || type==null)
        {
            type="其他";
        }
        biao.setUser(u);
        biao.setUrl(session.getAttribute("url").toString());
        biao.setDct(dt);
        biao.setBlog(blog);
      //  biao.setBlogid(id);
        biao.setType(type);
        biao.setStatus(false);
        biaoService.bsave(biao);
        return "redirect:/";
    }


    @GetMapping("/delete/{id}")
    public String deleteDct(Model model,@PageableDefault(size = 8, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable,@PathVariable Long id)
    {
        biaoRepository.delete(id);
        model.addAttribute("page",biaoRepository.findAll(pageable));
        return "admin/report";
    }


    @GetMapping("/deal/{id}")
    public String dealRepot(@PathVariable Long id,Model model)
    {
        System.out.println("deal in ");
        Biao biao=biaoRepository.findOne(id);
        if (biao!=null)
        {
            biao.setStatus(true);

        }
       Blog blog= blogService.getBlog(biao.getBlogid());
        User u=userRepository.findOne(blog.getUser().getId());
        userService.sendReportMail(biao.getUser().getId(),biao,false);
      //  userService.sendReportMail(u.getId(),biao);

        model.addAttribute("page",    biaoRepository.save(biao));
        model.addAttribute("message","已处理！！！");
        System.out.println("ready to  jump admin/report");
        return "redirect:/user/report";

    }

    @GetMapping("/reportBan/{id}")
    public String reportBan(@PathVariable Long id,Model model)
    {
        System.out.println("reportBan in ");
        Biao biao=biaoRepository.findOne(id);
        Blog blog= blogService.getBlog(biao.getBlogid());
        User u=userRepository.findOne(blog.getUser().getId());
        blog.setVisual(false);
        blogService.saveBlog(blog);
        biao.setStatus(true);
        userService.sendReportMail(biao.getUser().getId(),biao,true);
        userService.sendReportMail(u.getId(),biao,true);
        model.addAttribute("blog",blogService.getBlog(biao.getBlogid()));
        model.addAttribute("page",    biaoRepository.save(biao));
        model.addAttribute("message","已处理！！！");
        System.out.println("reportBan ready to  jump admin/report");
        return "redirect:/user/report";

    }



}
