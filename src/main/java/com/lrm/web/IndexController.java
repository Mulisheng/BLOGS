package com.lrm.web;

import com.lrm.NotFoundException;
import com.lrm.dao.HistoryRepository;
import com.lrm.dao.UserRepository;
import com.lrm.po.Blog;
import com.lrm.po.History;
import com.lrm.po.Type;
import com.lrm.po.User;
import com.lrm.service.*;
import com.lrm.vo.BlogQuery;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Array;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private HistoryService historyService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String index(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model, HttpSession session) {
        System.out.println("进index!!");
        model.addAttribute("page",blogService.listBlog(pageable));
        System.out.println(pageable);
        model.addAttribute("types", typeService.listTypeTop(6));
        for (Type t:typeService.listTypeTop(6)
             ) {
            System.out.println(t.getName());
        }
        System.out.println();
        model.addAttribute("tags", tagService.listTagTop(10));
        System.out.println("Come Blog ID   before");
        List<Blog> blogs=blogService.listRecommendBlogTop(5);
        System.out.println("Come blog ID:");
        for (Blog b:blogs
             ) {
            System.out.println(b.getId());
        }
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(5));
        System.out.println("index finish");

        if(session.isNew()){
            session.setAttribute("flag1",0);
        }

        if(session.isNew()||session.getAttribute("flag").equals(0))
        {
            User  turist=new User();
            session.setAttribute("user",turist);
//        User ur=(User)session.getAttribute("user");
            System.out.println("古德古德");
//            System.out.println("古德古德古德:"+session.getAttribute("user").toString());

            session.setAttribute("flag",0);
            System.out.println("古德古德session good:"+session.getAttribute("user").toString());
            ;
        }

        return "index";
    }


    @PostMapping("/search")
    public String search(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String query, Model model) {
        model.addAttribute("page", blogService.listBlog("%"+query+"%", pageable));
        model.addAttribute("query", query);
        return "search";
    }

    @Autowired
    private HistoryRepository historyRepository;
    /////在这里添加  HISTORY history
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id,Model model,HttpSession session) {
        model.addAttribute("blog", blogService.getAndConvert(id));
        if(session.getAttribute("flag").equals(1)){
//            先判断是否同样（因为我每次都保证不超过50）
//            if(same){
//                删掉相同
//            }else{
//                if 50
//                删尾
//            }
//            if(){
//
//            }

            User user=(User)session.getAttribute("user");
            System.out.println("user.id:"+user.getId() );
            History history2=historyService.check(user.getId(),id);
            System.out.println("还没进");
//            System.out.println("id:"+history2.toString());
            if(history2==null){
                System.out.println("1");
                    List<History> lh=historyRepository.findAll();
                System.out.println("2 if 50 before");
                   if(lh.size()==5){
                       System.out.println("50");
                       historyRepository.deleteByQueryQuery();
                       //                    delete rear
                   }else{
                       System.out.println("<50");
                   }
            }else{
                System.out.println("yes");
                //delete   by  id
                historyService.delete(history2.getId());
            }
//            System.out.println("history2:");
//            System.out.println(history2.toString());
            History history=new History();
            history.setBlogid(id);
            System.out.println("set blog id finish");
            User u=(User)session.getAttribute("user");
            history.setUserid(u.getId());
            historyRepository.save(history);
        }
        return "blog";
    }

    @GetMapping("/footer/newblog")
    public String newblogs(Model model) {
        model.addAttribute("newblogs", blogService.listRecommendBlogTop(3));
        return "_fragments :: newblogList";
    }

}
