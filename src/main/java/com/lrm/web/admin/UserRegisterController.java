package com.lrm.web.admin;

import com.lrm.dao.UserRepository;
import com.lrm.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


@Controller
@RequestMapping("/user")
public class UserRegisterController {
    @Value("${file.path}")
    private String filePath;

    //密码判断格式
    public static boolean isLetterDigit(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            } else if (Character.isLetter(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]{6,12}$";
        boolean isRight = isDigit && isLetter && str.matches(regex);
        return isRight;
    }

    public String encrytion(String s){
        char cs[]=s.toCharArray();
        for (int i=0;i<cs.length;i++) {
            cs[i] = (char) (cs[i] - 15);
        }
        s=new String(cs);
        String srevers=new StringBuffer(s).reverse().toString();
        return srevers;
    }


    @Autowired
    private UserRepository userRepository;
    @GetMapping("/res")
    public String userRegister(){
        return "register";
    }

    public static String reversel1(String str){
        return new StringBuffer(str).reverse().toString();
    }

    @PostMapping("/register")
    public String register(User user, RedirectAttributes attributes, @RequestParam(name = "file")MultipartFile file, Model model) throws Exception {
        System.out.println("进register");

       String extName= file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
       String fileName= UUID.randomUUID().toString()+extName;
        FileCopyUtils.copy(file.getInputStream(),new FileOutputStream(new File(filePath+fileName)));


        if(!isLetterDigit(user.getPassword())){
            attributes.addFlashAttribute("message", "密码6-12位，并且包含数字和字母");
            System.out.println("else");

            return "redirect:/user/res";
        }

        //加密
        user.setPassword(encrytion(user.getPassword()));
        SimpleDateFormat time = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        Date date=new Date();
        System.out.println("当前时间:"+time.format(date));
//        user.setAvatar(fileName);
        user.setCreateTime(date);
        user.setType(2);
        userRepository.save(user);

        return "redirect:/admin" ;
    }


    }

