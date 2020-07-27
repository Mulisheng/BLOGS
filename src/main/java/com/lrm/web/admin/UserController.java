package com.lrm.web.admin;

import com.lrm.dao.BiaoRepository;
import com.lrm.dao.BlogRepository;
import com.lrm.dao.CommentRepository;
import com.lrm.dao.UserRepository;
import com.lrm.po.Biao;
import com.lrm.po.Blog;
import com.lrm.po.Following;
import com.lrm.po.User;
import com.lrm.service.BiaoService;
import com.lrm.service.BlogService;
import com.lrm.service.FollowService;
import com.lrm.service.UserService;
import com.lrm.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private FollowService followService;
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BiaoRepository biaoRepository;
    @Value("${file.path}")
    private String filePath;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BiaoService biaoService;
    @Autowired
    private BlogService blogService;




    @GetMapping
    public String userLoginPage(HttpSession session){

        if ((session.getAttribute("flag").equals(1) || session.getAttribute("flag").equals(2)   ) && session.getAttribute("user") != null && session.getAttribute("user") != "")
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
//    System.out.println("ustatus:"+user.getStatus());

    if(user!=null && user.getStatus().equals(new Long(9999))){
        System.out.println("");
        attributes.addFlashAttribute("message", "账户已经冻结");
//        ystem.out.println("else");S

        return "redirect:/user";
    }

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
        System.out.println("来否来否");
        session.setAttribute("flag1",1);
        return "about";
    } else {
        attributes.addFlashAttribute("message", "用户名和密码错误");
        System.out.println("else");

        return "redirect:/user";
    }
}


    @PostMapping("/useredit/{id}")
    public String userEdit(@PathVariable Long id,User user,RedirectAttributes attributes,HttpSession session,@RequestParam(name = "file") MultipartFile file) throws IOException {
        User t=userRepository.findOne(id);
        System.out.println(user);

      String fileName="";


        if (file.isEmpty()) {
            fileName =t.getAvatar();
        }else {
            String extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")); //拿到文件扩展名
            fileName = UUID.randomUUID().toString() + extName; //uuid+扩展名作为name
            FileCopyUtils.copy(file.getInputStream(),new FileOutputStream(new File(filePath+fileName)));//FileOutputStream存放位置

        }
        User user2=(User)session.getAttribute("user");
        commentRepository.updateavatar(fileName,user2.getId());
//        commentRepository.save();
        System.out.println("id" + id);
        System.out.println(" 进/useredit/{id} ");
        System.out.println(fileName);
        System.out.println(user);

        User user1=userRepository.findOne(id);
      //  System.out.println(user1);
        SimpleDateFormat time=new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        Date date=new Date();
        System.out.println("当前时间:"+time.format(date));

        user1.setAvatar(fileName);
        user1.setNickname(user.getNickname());
        user1.setEmail(user.getEmail());
        user1.setUsername(user.getUsername());
        user1.setUpdateTime(date);
      //  System.out.println(user1);
        userRepository.save(user1);
        session.setAttribute("user",userRepository.findOne(id));
//        attributes.addAttribute("userImg",userRepository.findOne(id));
        //System.out.println(session.getAttribute("user"));
        attributes.addFlashAttribute("message","修改信息成功");

            return "redirect:/about";
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
                                 HttpSession session) {

        String oldPwd = encrytion(pwd);
        String newPwd = encrytion(npwd);
        System.out.println("/updatepassword/{id}");
        System.out.println("id" + id);
        System.out.println("pwd" + pwd);

        User user = userRepository.getOne(id);



        if (user.getPassword().equals(oldPwd)) {
            System.out.println("equals");

            if (npwd != null && npwd != "" && npwd.equals(cpwd)) {
                System.out.println(" 2 equals");
                user.setPassword(newPwd);
                userRepository.save(user);
                attributes.addFlashAttribute("message", "密码修改成功,请重新登陆");
                session.removeAttribute("user");
                if (session.getAttribute("flag1").equals(2)){
                    session.setAttribute("flag",0);
                    return "redirect:/admin";
                }
                else if (session.getAttribute("flag1").equals(1)){
                    session.setAttribute("flag",0);
                    return "redirect:/user";
                }
            } else {
                System.out.println(" 2 not equals");
                attributes.addFlashAttribute("message", "修改失败，密码不一致");
                return "redirect:/user/updatepassword/{id}";
            }
        } else {
            System.out.println("not equals");
            attributes.addFlashAttribute("message", "原密码不一致");
            return "redirect:/user/updatepassword/{id}";

        }
        return "redirect:/user";
    }

    @GetMapping("/other/{id}")
    public String otherUser(@PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable,@PathVariable Long id, User otherUser,HttpSession session,Model model)
    {
        otherUser=userRepository.findOne(id);
      //  System.out.println("otherUser"+otherUser);
        User u=(User) session.getAttribute("user");
       // System.out.println(followService.checkFollowing(u.getId(),id));
        Boolean followingStatus;
        if (followService.checkFollowing(u.getId(),id)!=null)
        {
            followingStatus=true;
        }
        else
        {
            followingStatus=false;
        }


        model.addAttribute("followingStatus",followingStatus);
        model.addAttribute("page",blogService.listPost(pageable,id));
        session.setAttribute("otherUser",otherUser);
        return "/xman";
    }


    @PostMapping("/xmanBlogs")
    public String xmanBlogs(HttpSession session,@PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable, Model model, Blog blog)
    {
        System.out.println(blog);
        User u=(User) session.getAttribute("otherUser");
        model.addAttribute("page",blogService.listPost(pageable,u.getId()));
        return "xman::xmanList";
    }


    @GetMapping("/following/{id}")
    public  String following(@PathVariable Long id, HttpSession session, Model model
            , @PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable
            , Following following)
    {
        System.out.println("进关注");

        User u=(User) session.getAttribute("user");
       User otherUser=userRepository.findOne(id);
       following.setUser(u);
       following.setSelf(u.getUsername());
       following.setFollowingId(otherUser.getId());
       following.setFollowName(otherUser.getUsername());
       followService.save(following);
     //   System.out.println(followService.find(u.getId()));
//       model.addAttribute("following",followService.find(u.getId()));
        //  System.out.println("otherUser"+otherUser);
        Boolean followingStatus;
        if (followService.checkFollowing(u.getId(),id)!=null)
        {
            followingStatus=true;
        }
        else
        {
            followingStatus=false;
        }
        model.addAttribute("followingStatus",followingStatus);
        model.addAttribute("page",blogService.listPost(pageable,id));
        session.setAttribute("otherUser",otherUser);
        return "/xman";
    }


    @GetMapping("/deleteFollowing/{id}")
    public  String deleteFollowing(@PathVariable Long id, HttpSession session, Model model
            , @PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable
            , Following following)
    {
        System.out.println("取消关注");

        User u=(User) session.getAttribute("user");
        User otherUser=userRepository.findOne(id);
        Following f=followService.checkFollowing(u.getId(),otherUser.getId());
        followService.deleteFollowing(f.getId());
        Boolean followingStatus;
        if (followService.checkFollowing(u.getId(),id)!=null)
        {
            followingStatus=true;
        }
        else
        {
            followingStatus=false;
        }
        model.addAttribute("followingStatus",followingStatus);
        model.addAttribute("page",blogService.listPost(pageable,id));
        session.setAttribute("otherUser",otherUser);
        return "/xman";
    }


    @GetMapping("/listFollow/{id}")
    public String listFollow(@PageableDefault(size = 5, sort = {"id"}
    , direction = Sort.Direction.ASC) Pageable pageable
            ,HttpSession session,Model model,@PathVariable Long id)
    {
        model.addAttribute("page",followService.listFollowing(pageable,id));
        return "follow";
    }

    @PostMapping("/listFollow")
    public String refreshFollow(@PageableDefault(size = 5, sort = {"id"}
            , direction = Sort.Direction.ASC) Pageable pageable
            ,HttpSession session,Model model,Following following)
    {

        User u=(User)session.getAttribute("user");
        model.addAttribute("page",followService.listFollowing2(pageable,u.getId(),following));
        return "follow::followList";
    }


    @GetMapping("/cancelFollowing/{id}")
    public String cancelFollowing(@PathVariable Long id,HttpSession session
            ,@PageableDefault(size = 5, sort = {"id"}
            , direction = Sort.Direction.ASC) Pageable pageable
    ,Model model,Following following)
    {
        System.out.println("列举取消关注");

        User u=(User) session.getAttribute("user");
        User otherUser=userRepository.findOne(id);
        Long othid=otherUser.getId();
        Long Uid=u.getId();
        System.out.println("uid"+Uid);
        System.out.println("othid"+othid);
        System.out.println("id"+id);
        Following f=followService.checkFollowing(u.getId(),otherUser.getId());
        Long fid=f.getId();
        System.out.println("fid"+ fid);
        followService.deleteFollowing(f.getId());
        Boolean followingStatus;
        if (followService.checkFollowing(u.getId(),id)!=null)
        {
            followingStatus=true;
        }
        else
        {
            followingStatus=false;
        }
        model.addAttribute("followingStatus",followingStatus);
        model.addAttribute("page",followService.listFollowing2(pageable,u.getId(),following));
      //  model.addAttribute("page",blogService.listPost(pageable,id));
       // session.setAttribute("otherUser",otherUser);
         return "follow";
    }


    @GetMapping("/main")
    public String usermain()
    {
        System.out.println("/user/main");
        return "admin/usermain";
    }


    @RequestMapping("/report")
    public String report(Model model,@PageableDefault(size = 8, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable)
    {
        System.out.println("进入 report");
//        Pageable pageable=new PageRequest(0,5);


        model.addAttribute("page",biaoRepository.findAll(pageable));
        System.out.println(biaoRepository.findAll(pageable).getTotalPages());
        System.out.println(biaoRepository.findAll(pageable));
        return "admin/report";
    }

    public String decode(String s){
        String srevers=new StringBuffer(s).reverse().toString();;
        char cs[]=srevers.toCharArray();
        for (int i=0;i<cs.length;i++) {
            cs[i] = (char) (cs[i] + 15);
        }
        srevers=new String(cs);

        return srevers;
    }


    @PostMapping("/findPwd")
    public String findPwd(@RequestParam(name = "username") String userName,@RequestParam(name = "email") String email,RedirectAttributes attributes)
    {
        if (userService.checkUserName(userName) == null)
        {
            attributes.addFlashAttribute("message","用户名不存在");
            return "redirect:/user";
        }
        else if (userService.checkUserName(userName)!= null)
        {
            User uu=userService.checkUserName(userName);
            if (!email.equals(uu.getEmail()))
            {
                attributes.addFlashAttribute("message","邮箱与注册时所填邮箱不一致");
                return "redirect:/user";
            }
            else if (email.equals(uu.getEmail()))
            {
                String pwd=decode(uu.getPassword());
                System.out.println(pwd);
                userService.findPwd(uu.getUsername(),uu.getEmail(),pwd);
                attributes.addFlashAttribute("message","密码已发送至邮箱");
                return "redirect:/user";
            }
        }
        return "redirect:/user";



    }



    @GetMapping("/findPasswd")
    public  String reFindPwd()
    {
        return "findPwd";
    }





}
