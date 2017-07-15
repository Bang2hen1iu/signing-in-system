package com.xuemiao;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by dzj on 9/30/2016.
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("/index/index_v2");
        registry.addViewController("/sign_in_info").setViewName("/index/sign_in_info_v2");
        registry.addViewController("/rank_list").setViewName("/index/rank_list_v2");
        registry.addViewController("/week_plan").setViewName("/index/week_plan");
        registry.addViewController("/sign_in_action").setViewName("/index/sign_in_action");
        registry.addViewController("/admin/login").setViewName("/admin/login");
        registry.addViewController("/admin").setViewName("/admin/index");
        registry.addViewController("/admin_welcome").setViewName("/admin/welcome");
        registry.addViewController("/students").setViewName("/admin/students");
        registry.addViewController("/courses").setViewName("/admin/courses");
        registry.addViewController("/duty_students").setViewName("/admin/duty_students");
        registry.addViewController("/statistics").setViewName("/admin/statistics");
        registry.addViewController("/semesters").setViewName("/admin/semesters");
    }
}
