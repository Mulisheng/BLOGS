package com.lrm.web.admin;

import com.lrm.dao.CollectRepository;
import com.lrm.po.Collect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/collect")
public class CollectController {
    @Autowired
    CollectRepository collectRepository;

    @PostMapping("/add/{userid}/{blogid}/{title}")
    public String addCollection(@PathVariable(name = "userid") Long userid,
                                @PathVariable(name = "blogid") Long blogid,
                                @PathVariable(name = "title") String title,
                                RedirectAttributes attributes) {
        Collect ct=new Collect();
        ct.setBlogid(blogid);
        ct.setTitle(title);
        ct.setUserid(userid);

        collectRepository.save(ct);
        attributes.addFlashAttribute("message","收藏成功");

        return "redirect:/";
    }

}
