package com.darryl.hotswap.controller;

import com.darryl.hotswap.config.HotSwap;
import com.darryl.hotswap.service.ServiceTest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: darrylsun
 * @Description:
 * @Date: 2021/06/10
 */
@RestController
@Api(tags = "hot swap test")
@Slf4j
public class DemoHotSwapController {

    @Resource
    private HotSwap hotSwap;

    @GetMapping("/test")
    @ApiImplicitParam(paramType = "query", dataType = "string", name = "className", value = "类名", required = true)
    public String getUserInfo(String className) {
        ServiceTest serviceTest = (ServiceTest) hotSwap.newInstance(className);
        String value = "init";
        if (serviceTest != null) {
            value = serviceTest.getValue();
        }
        return value;
    }
}
