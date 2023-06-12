package com.example.junior;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan(basePackages = {"com.example.junior"})
@SpringBootApplication(exclude = PageHelperAutoConfiguration.class)
@MapperScan("com.example.junior.mapper")
public class WebStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebStarterApplication.class, args);
    }

}
