package com.lrm.web.admin;

import com.lrm.dao.BiaoRepository;
import com.lrm.dao.BlogRepository;
import com.lrm.po.Blog;
import com.lrm.po.CommonResult;
import com.lrm.po.Type;
import com.lrm.po.User;
import com.lrm.service.BlogService;
import com.lrm.service.TagService;
import com.lrm.service.TypeService;
import com.lrm.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by limi on 2017/10/15.
 */
@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";
    @Value("${file.path}")
    private String filePath;

    @Autowired
    private BiaoRepository biaoRepository;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog, Model model,HttpSession session) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        if (session.getAttribute("flag").equals(2)) {
            return LIST;
        }
        else if(session.getAttribute("flag").equals(1)){
            return "redirect:/userPost";
        }
        return "redirect:/userPost";
    }


    //传入参数 Pageable  BlogQuery
    @GetMapping("/user/blogs/{id}")
    public String userBlogs(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,@PathVariable Long id,Model model,BlogQuery blog)
    {
        System.out.println("1");
        model.addAttribute("types", typeService.listType());
        System.out.println("mid");
        model.addAttribute("page", blogService.listBlog3(pageable, blog,id));
        System.out.println("2");
        return "userPost";
    }

    @RequestMapping("/report")
    public String search1()
    {
        System.out.println("inininin");
        return "admin/report";
    }


    @PostMapping("/userPost")
    public String postList(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog, Model model,HttpSession session) {
        User u=(User) session.getAttribute("user");
        model.addAttribute("page", blogService.listBlog3(pageable, blog,u.getId()));
//        userid=1l;
//
//        System.out.println(userid);
//        System.out.println(blogService.listPost(pageable,userid));
//        Page page=blogService.listPost(pageable,userid);
//        System.out.println(page.getTotalPages());
//        System.out.println(page.getTotalElements());
        return "userPost:: postList";
    }


    //blogs js里面通过lodata 传入BlogQuery
    //        title : $("[name='title']").val(),
    //        typeId : $("[name='typeId']").val(),
    //        recommend : $("[name='recommend']").prop('checked'),
    //        page : $("[name='page']").val()

    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog, Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blogs :: blogList";
    }


    @GetMapping("/blogs/input")
    public String input(Model model,HttpSession session) {
        setTypeAndTag(model);
        model.addAttribute("blog", new Blog());
        return INPUT;
    }

    private void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }


    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        setTypeAndTag(model);
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        return INPUT;
    }

    public   CommonResult maxUtilStr(String str1, String str2) {
        CommonResult commonResult = new CommonResult();
        //把字符串转成字符数组
        char[] arr1 = str1.toCharArray();
        char[] arr2 = str2.toCharArray();
        // 把两个字符串分别以行和列组成一个二维矩阵
        int[][] temp = new int[arr1.length][arr2.length];
        // 存储最长公共子串长度
        int length = 0;
        //start表明最长公共子串的起始点，end表明最长公共子串的终止点
        int end = 0;
        int start = 0;
        ////初始化二维矩阵中的第一行
        for (int i = 0; i < arr2.length; i++) {
            temp[0][i] = (arr1[0] == arr2[i]) ? 1 : 0;
        }
        //初始化二维矩阵中的第一列
        for (int j = 0; j < arr1.length; j++) {
            temp[j][0] = (arr2[0] == arr1[j]) ? 1 : 0;
        }
        //嵌套for循环：比较二维矩阵中每个点对应行列字符中否相等，相等的话值设置为1，否则设置为0
        for (int i = 1; i < arr1.length; i++) {
            for (int j = 1; j < arr2.length; j++) {
                if (arr1[i] == arr2[j]) {
                    temp[i][j] = temp[i - 1][j - 1] + 1;

                    if (temp[i][j] > length) {
                        length = temp[i][j];
                        end = j;
                    }
                } else {
                    temp[i][j] = 0;
                }
            }
        }
        //注意********************************长度一样的可能有多个，现在获取的是最后一个，问题不大

        //求出最长公共子串的起始点

        start = end - length + 1;
        StringBuilder sb = new StringBuilder();
        //通过查找出值为1的最长对角线就能找到最长公共子串
        for (int j = start; j < end + 1; j++) {
            sb.append(arr2[j]);
        }
        commonResult.setCommonCount(length);
        commonResult.setCommonStr(sb.toString());
        return commonResult;

    }

    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session, @RequestParam(name = "flag") String flag,@RequestParam(name = "file") MultipartFile file) throws Exception {
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        System.out.println("进入bloginput");

        String fileName="";
        if (file.isEmpty()) {
            if(blog.getId().equals(null) || blog.getId().equals("")){
                fileName ="";
            }else{
                fileName=blog.getFirstPicture();
            }


        }else {
            String extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")); //拿到文件扩展名
            fileName = UUID.randomUUID().toString() + extName; //uuid+扩展名作为name
            FileCopyUtils.copy(file.getInputStream(),new FileOutputStream(new File(filePath+fileName)));//FileOutputStream存放位置

        }

        blog.setFirstPicture(fileName);
        System.out.println("文件 第一个图片"+fileName);

        System.out.println("传过来的flag+"+flag);
        if( flag.equals("") || flag==null)
        {
            flag="原创";
        }
        blog.setFlag(flag);
        Blog b;
        System.out.println("传过来的flag+"+flag);

        List<Blog>  blogList=blogRepository.findAll();

        CommonResult commonResult=new CommonResult();

        System.out.println("blog.getTitle().length():"+blog.getTitle().length());
        System.out.println("same length:"+commonResult.getCommonCount());
        for (Blog bb:blogList
        ) {
            if(blog.getId()==null ||  blog.getId()!=bb.getId())
            commonResult=maxUtilStr(blog.getTitle(),bb.getTitle());
            if(commonResult.getCommonCount()*1.0/blog.getTitle().length()>=0.8){
                blog.setRr(1);
                break;
            }
        }

        if (blog.getId() == null) {

//            System.out.println(blog.getFlag());
            blog.setViews(1);
            b =  blogService.saveBlog(blog);
           // System.out.println(b);
        } else {
            b = blogService.updateBlog(blog.getId(), blog);
        }

        if (b == null ) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
            return REDIRECT_LIST;

    }

    @Transactional
    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes,HttpSession session) {
        Blog b=blogRepository.findOne(id);
        blogRepository.delete(b);
//        blogService.deleteBlog(b);
        attributes.addFlashAttribute("message", "删除成功");
        if (session.getAttribute("flag").equals(2))
        return REDIRECT_LIST;
        else
        {
            return "redirect:/userPost";
        }
    }

    @Transactional
    @GetMapping("/blogs/{id}/delete2")
    public String delete2(@PathVariable Long id,RedirectAttributes attributes,HttpSession session) {
        Blog b=blogRepository.findOne(id);
        if(b.isVisual()){
            b.setVisual(false);
        }
        else{
            b.setVisual(true);
        }

        blogRepository.save(b);
//        blogRepository.delete(b);
//        blogService.deleteBlog(b);
        attributes.addFlashAttribute("message", "屏蔽成功");
        if (session.getAttribute("flag").equals(2))
            return REDIRECT_LIST;
        else
        {
            return "redirect:/userPost";
        }
    }

    @GetMapping("tagBlogList")
    public String tagBlogList(@PageableDefault(size = 5, direction = Sort.Direction.DESC) Pageable pageable,Blog blog)
    {
        System.out.println("进tagBlogList");
        System.out.println(blog);
        System.out.println(blog.getType().getId());
        return "tags::tagBlogList";
    }



}
