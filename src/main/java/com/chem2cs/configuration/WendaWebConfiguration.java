package com.chem2cs.configuration;

import com.chem2cs.interceptor.LoginRequriedInterceptor;
import com.chem2cs.interceptor.PassportInterceptor;
import com.sun.org.apache.xpath.internal.SourceTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class WendaWebConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    PassportInterceptor passportInterceptor;
    @Autowired
    LoginRequriedInterceptor loginRequriedInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginRequriedInterceptor).addPathPatterns("/user/*");
        super.addInterceptors(registry);
    }
}
