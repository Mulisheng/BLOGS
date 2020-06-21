package com.lrm.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class DefaultView extends WebMvcConfigurerAdapter {

//    @Override
//    public void addViewControllers(ViewControllerRegistry reg) {
//        reg.addViewController("/").setViewName("register");//默认访问页面
//        reg.setOrder(Ordered.HIGHEST_PRECEDENCE);//最先执行过滤
//        super.addViewControllers(reg);
//    }
}


