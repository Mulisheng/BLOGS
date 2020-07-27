package com.lrm.web.admin;

import com.lrm.dao.CollectRepository;
import com.lrm.po.Blog;
import com.lrm.po.Collect;
import com.lrm.service.BlogService;
import com.lrm.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/collect")
public class CollectController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private  CollectRepository collectRepository;
    @Autowired
    private CollectService collectService;

    @PostMapping("/add/{userid}/{blogid}/{title}")
    public String addCollection(@PathVariable(name = "userid") Long userid,
                                @PathVariable(name = "blogid") Long blogid,
                                @PathVariable(name = "title") String title,
                                RedirectAttributes attributes) {
        Blog blog=blogService.getBlog(blogid);
        System.out.println(blogid);
        System.out.println("blog.getId"+blog.getId());
        Collect ct=new Collect();
        ct.setBlog(blog);
        ct.setBlogid(blogid);
        ct.setTitle(title);
        ct.setUserid(userid);

        collectRepository.save(ct);
        attributes.addFlashAttribute("message","收藏成功");

        return "redirect:/";
    }

    //这里  LOOK
    @GetMapping("/collection/{id}")
    public String myCollection(@PageableDefault(size = 8, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable
            , @PathVariable Long id, Model model)
    {
        model.addAttribute("page",collectService.listCollection(pageable,id));
        model.addAttribute("id",id);
        System.out.println(id);
//        System.out.println(collectService.listCollection(pageable,id));
        return "redirect:/myCollection";
    }


    @PostMapping("collectPost/{id}")
    public String collectPost(@PageableDefault(size = 8, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable
            , Model model,@RequestParam(name = "id") Long id
                              )
    {
        System.out.println(id);
        model.addAttribute("page",collectService.listCollection(pageable,id));

        return "myCollect :: collectList";
    }

}
