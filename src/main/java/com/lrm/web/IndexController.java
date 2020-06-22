package com.lrm.web;

import com.lrm.dao.BiaoRepository;
import com.lrm.dao.CollectRepository;
import com.lrm.dao.HistoryRepository;
//import com.lrm.dao.ReportRepository;
import com.lrm.dao.RtRepository;
import com.lrm.po.*;
import com.lrm.service.*;
import com.lrm.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private RtService rtService;

    @Autowired
    private CollectService collectService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;
    @Autowired
    private BiaoService biaoService;

    @Autowired
    private RtRepository reportRepository;

    @Autowired
    private BiaoRepository biaoRepository;
    @RequestMapping("/dct2222")
    private String dct(){
        System.out.println("report in");
        return "report";
    }

    @RequestMapping("/dct12222")
    private String dct1(Biao biao, RedirectAttributes attributes)throws Exception{
        System.out.println("report1 in");
//        System.out.println(r);
//        Long a=new Long(1);
//        report.setId(a);
//        Report report1=reportRepository.getOne(a);
//        System.out.println(report1)

//        report.setDes(des);
//        System.out.println(report.toString());
//        System.out.println("save before");
//        if(report!=null){
//            System.out.println("you");
//        }
//        rtService.s(report);

//        System.out.println(reportRepository.save(r));
//        System.out.println(dt);
//        biao.setDct(dt);
        System.out.println("biao.getDct:"+biao.getDct());
        biaoService.bsave(biao);
        attributes.addFlashAttribute("message", "提交成功");
        System.out.println("跳转前");
        //对数据进行处理
        return "redirect:/about";
    }


    @RequestMapping("/recommentblog/{id}")
    public String recomment(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                       @PathVariable Long id, Model model,BlogQuery blog) {
        System.out.println("recommentblog in:");
        List<Tag> tags = tagService.listTagTop(10000);
        if (id == -1) {
            id = tags.get(0).getId();
        }
        Page<Blog> b=blogService.listBlog2(pageable,blog);
        System.out.println("++++++Blog:");
        for (Blog bb:b
        ) {
            System.out.println(bb);
        }




        model.addAttribute("tags", tags);
        model.addAttribute("page", blogService.listBlog2(pageable,blog));
        model.addAttribute("activeTagId", id);
        System.out.println("rerererere");
        return "recommentblog";
    }

//    private String recomment(){
//        System.out.println("rerererere");
//        return "recommentblog";
//    }

    @RequestMapping("/userPost")
    public String post(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                       BlogQuery blog, Model model){
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "userPost";
    }

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
            if(session.getAttribute("flag").equals(0)){
                model.addAttribute("collect",0);
            }else{
                System.out.println("uid:"+user.getId());

                Collect cc=collectService.checkCollect(id,user.getId());

                System.out.println(cc);
                if ( cc!= null) {
                    Collect c = collectService.checkCollect(id, user.getId());
                    model.addAttribute("collect",1);
                }
                else{
                    model.addAttribute("collect",0);
                }
            }


//            System.out.println("user.id:"+user.getId() );
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
