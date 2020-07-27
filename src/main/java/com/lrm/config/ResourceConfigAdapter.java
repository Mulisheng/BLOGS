package com.lrm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@Configuration
public class ResourceConfigAdapter extends WebMvcConfigurerAdapter {

        //添加虚拟路径映射
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/images/**").addResourceLocations("file:F:\\BLOGS\\7-2-ｍｏｒｎｉｎｇ＇\\BLOGS\\src\\main\\resources\\static\\images\\");
        }
    }





