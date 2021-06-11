package com.darryl.hotswap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author: darrylsun
 * @Description:
 * @Date: 2021/06/10
 */
@SpringBootApplication
@EnableScheduling
@EnableSwagger2
public class HotSwapApplication {
    public static void main(String[] args) {
        SpringApplication.run(HotSwapApplication.class, args);
    }
}
