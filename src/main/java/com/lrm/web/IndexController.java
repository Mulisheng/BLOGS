package com.lrm.web;

import com.lrm.dao.*;
import com.lrm.po.*;
import com.lrm.service.*;
import com.lrm.vo.BlogQuery;
import com.lrm.vo.UserQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//import com.lrm.dao.ReportRepository;

@Controller
public class IndexController {
    @Value("${file.path}")
    private String filePath;
    @Autowired
    private  BlogRepository blogRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CollectRepository collectRepository;

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

    @Autowired
    private  UserService userService;


    @RequestMapping("/commentadmin")
    public String commentadmin(){
        System.out.println("commentadmin！！！");
        return "redirect:/";
    }
    @RequestMapping("/admin2/report")
    public String gogo(Model model,@PageableDefault(size = 8, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable,Biao biao)
    {
        System.out.println("gogo");
//        Pageable pageable=new PageRequest(0,5);
      //  System.out.println(type);
       // biao.setType("色");
        System.out.println(biao);
        model.addAttribute("page",biaoService.listReport(pageable,biao));
        System.out.println(biaoService.listReport(pageable,biao).getTotalPages());
        System.out.println(biaoService.listReport(pageable,biao).getTotalElements());
        return "admin/report :: ccList";
    }


    @RequestMapping("/ban/{id}")
    public String ban(@PageableDefault(size = 8, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                            Model model,@PathVariable(name = "id") Long id) {
        userService.updateUser(id);
        User user=userRepository.getOne(id);
        userService.toMail(user.getEmail(),user.getUsername(),user.getStatus(),user);
        System.out.println("save end");
//        model.addAttribute("types", typeService.listType());
        System.out.println("list before");
        model.addAttribute("page", userService.listUser1(pageable));
        System.out.println("ban  ++++++++++++++++++++list end  go to user");
        return "admin/users";
    }

    @RequestMapping("/usershow")
    public String search(@PageableDefault(size = 8, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                         UserQuery userQuery, Model model,HttpSession session) {

        model.addAttribute("types", typeService.listType());
        System.out.println("list before");
        model.addAttribute("page", userService.listUser(pageable, userQuery,session));
        System.out.println("list end  go to user");
        return "admin/users";
    }

    @RequestMapping("/usershow1")
    public String search1(@PageableDefault(size = 8, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                         User user , Model model,HttpSession session) {
       // model.addAttribute("types", typeService.listType());
        System.out.println("list before");
        System.out.println(user);
        System.out.println("list after");
        model.addAttribute("page", userService.listUserSearch(pageable, user));
        System.out.println("list end  go to user");
        return "admin/users :: userList";
    }



    @RequestMapping("/jump")
    public String jumptomyCollection(@PageableDefault(size = 8, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable, Model model,HttpSession session)
    {
        User u=(User)session.getAttribute("user");
        model.addAttribute("page",collectService.listCollection(pageable,u.getId()));
//        model.addAttribute("id",id);
//        System.out.println(id);
        return "myCollection";
    }

    @RequestMapping("/deletecollect/{id}/{blogid}")
    public String deletecollect(@PathVariable(name = "id") Long id,@PathVariable(name = "blogid") Long blogid,HttpSession session){
        session.setAttribute("showblogid",blogid);
        System.out.println("id:"+id);
            collectRepository.delete(id);

        return "redirect:/jump";
    }

    @GetMapping("/deletereport/{id}")
    public String deleteDct(Model model,@PageableDefault(size = 8, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable,@PathVariable Long id)
    {
        biaoRepository.delete(id);
        model.addAttribute("page",biaoRepository.findAll(pageable));
        return "admin/report :: cclist";
    }






    @RequestMapping("/collect1")
    public String myCollection1(@PageableDefault(size = 8, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable, Model model,HttpSession session,Collect collect)
    {
        System.out.println("collect1");
        User u=(User)session.getAttribute("user");
//        model.addAttribute("page", collectService.listCollection(pageable, u.getId()));
        System.out.println(collect);
        model.addAttribute("page", collectService.listCollection2(pageable, collect,u.getId()));
        System.out.println(collectService.listCollection2(pageable,collect,u.getId()));
        System.out.println("collect1 gogogo");
        return "myCollection :: collectList";
    }


    @GetMapping("/collection/{id}")
    public String myCollection(@PageableDefault(size = 8, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable, @PathVariable Long id, Model model,HttpSession session)
    {

        model.addAttribute("page",collectService.listCollection(pageable,id));
        model.addAttribute("id",id);

        System.out.println(id);
        System.out.println("gugugugu111++");
//        System.out.println(collectService.listCollection(pageable,id));
        return "myCollection";
    }

    @RequestMapping("/myCollection")
    public String myCollection(){
        return "myCollection";
    }



    @RequestMapping("/dct2222")
    private String dct(){
        System.out.println("report in");
        return "report";
    }

    @RequestMapping("/dct12222")
    private String dct1(Biao biao, RedirectAttributes attributes)throws Exception{
        System.out.println("report1 in");
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
        if(tags.size()!=0)
        if (id == -1) {
            id = tags.get(0).getId();
        }


        model.addAttribute("tags", tags);
        model.addAttribute("page", blogService.listBlog2(pageable,blog));
        model.addAttribute("activeTagId", id);
        System.out.println("rerererere");
        return "recommentblog";
    }

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


        User user=(User)session.getAttribute("user");
        if(session.getAttribute("flag").equals(0)){
            model.addAttribute("collect",0);
        }else{
            System.out.println("uid:"+user.getId());

            Collect cc=collectService.checkCollect(id,user.getId());

            System.out.println(cc);
            if ( cc!= null) {
                Collect c = collectService.checkCollect(id, user.getId());
                model.addAttribute("collect",0);
            }
            else{
                model.addAttribute("collect",1);
            }
        }



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
            Blog blog=blogRepository.findOne(id);
            history.setTitle(blog.getTitle());
            history.setBlogname(blog.getUser().getUsername());
            System.out.println("uuuuu:"+blog.getUser().getUsername());

            historyRepository.save(history);
        }
        System.out.println("get away!!");
        Blog blog=blogRepository.findOne(id);
        session.setAttribute("blogid",blog.getUser().getId());
        return "blog";
    }

    @GetMapping("/footer/newblog")
    public String newblogs(Model model) {
        model.addAttribute("newblogs", blogService.listRecommendBlogTop(3));
        return "_fragments :: newblogList";
    }


    //回显
    @GetMapping("/personalhistory")
    public String personalhistory(Model model,@PageableDefault(size = 8, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable,HttpSession session){
        History h1=new History();
        User user=(User)session.getAttribute("user");
        h1.setUserid(user.getId());
        model.addAttribute("page", historyService.listHistory(pageable,h1));
        return "personalhistory";
    }

    //回显
    @RequestMapping("/personalhistory1")
    public String personalhistory1(Model model,@PageableDefault(size = 8, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable,HttpSession session){
        History h1=new History();
        User user=(User)session.getAttribute("user");
        h1.setUserid(user.getId());
        model.addAttribute("page", historyService.listHistory(pageable,h1));
        return "personalhistory :: blogList";
    }




    @PostMapping(value = "/uploadfile")
    public @ResponseBody
    Map<String,Object> demo(@RequestParam(value = "editormd-image-file", required = false) MultipartFile file, HttpServletRequest request) {
        Map<String,Object> resultMap = new HashMap<String,Object>();




        //保存
        try {
//            File imageFolder= new File(request.getServletContext().getRealPath("static/images"));
//            File targetFile = new File(imageFolder,file.getOriginalFilename());
//            if(!targetFile.getParentFile().exists())
//                targetFile.getParentFile().mkdirs();
//            file.transferTo(targetFile);
//            BufferedImage img = ImageUtil.change2jpg(targetFile);
//            ImageIO.write(img, "jpg", targetFile);
            /*            file.transferTo(targetFile);*/
//            byte[] bytes = file.getBytes();
//            Path path = Paths.get(realPath + file.getOriginalFilename());
//            Files.write(path, bytes);
            String extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")); //拿到文件扩展名
            String fileName = UUID.randomUUID().toString() + extName; //uuid+扩展名作为name
            FileCopyUtils.copy(file.getInputStream(),new FileOutputStream(new File(filePath+fileName)));//FileOutputStream存放位置
            String url = "/images/"
                    + fileName;


            resultMap.put("success", 1);
            resultMap.put("message", "上传成功！");
            resultMap.put("url",url);
            System.out.println(url+fileName);
        } catch (Exception e) {
            resultMap.put("success", 0);
            resultMap.put("message", "上传失败！");
            e.printStackTrace();
        }
        System.out.println(resultMap.get("success"));
        return resultMap;
    }




}
