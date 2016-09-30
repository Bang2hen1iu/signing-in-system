package com.xuemiao;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

/**
 * Created by dzj on 10/1/2016.
 */
@Configuration
public class JerseyConfiguration extends ResourceConfig{
    public JerseyConfiguration(){
        packages(true, "com.xuemiao.api");
    }
}
