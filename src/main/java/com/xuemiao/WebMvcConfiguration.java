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
        registry.addViewController("/").setViewName("index/index");
        registry.addViewController("/sign_in_info").setViewName("index/sign_in_info");
        registry.addViewController("/rank_list").setViewName("index/rank_list");
        registry.addViewController("/sign_in_action").setViewName("index/sign_in_action");
        registry.addViewController("/admin/login").setViewName("admin/login");
        registry.addViewController("/out_of_date").setViewName("outOfDate");
    }
}
