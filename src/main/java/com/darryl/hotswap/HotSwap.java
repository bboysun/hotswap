package com.darryl.hotswap;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: darrylsun
 * @Description: hot swap main function
 * @Date: 2021/06/10
 */
@Slf4j
public class HotSwap {

    private static final long LOADER_INTERVAL = 3;
    private static URLClassLoader classLoader;

    // 可动态配置获取，同时可以是一个列表不单单是一个jar
    private static final String HOT_UPDATE_JAR_PATH = "";
    // 可以用更新时间或者jar包的MD5串进行匹配校验，此处简单处理，可以从DB中动态获取
    private static Long currentJarTime = 0L;

    private ScheduledExecutorService scheduledExecutorService;

    @PostConstruct
    public void init() {
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(()->{
            if (isHotUpdate()) {
                reload();
            }
        }, 0, LOADER_INTERVAL, TimeUnit.SECONDS);
    }

    // 动态获取新实例，注意返回值可能为null，调用者要加判断
    public static Object newInstance(String className) {
        if (classLoader != null) {
            try {
                synchronized (HotSwap.class) {
                    Object newInstance = Class.forName(className, true, classLoader).newInstance();
                    return newInstance;
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 加载jar文件
     */
    private void reload() {
        File jarPath = new File(HOT_UPDATE_JAR_PATH);
        if (jarPath.exists()) {
            log.info("jar last modified is {}", jarPath.lastModified());
            try {
                synchronized (HotSwap.class) {
                    classLoader = new URLClassLoader(new URL[]{jarPath.toURI().toURL()});
                }
            } catch (Exception e) {
                log.error("new classLoader instance Exception: ", e);
            }
        } else {
            log.info("dont find jar file...please check your hot update jar path");
        }
    }

    /**
     * 判断是否要热加载
     * @return
     */
    private boolean isHotUpdate() {
        File hotLoaderFile = new File(HOT_UPDATE_JAR_PATH);
        boolean isHotUpdate = false;
        if (hotLoaderFile.exists()) {
            long modifyTime = hotLoaderFile.lastModified();
            isHotUpdate = currentJarTime != modifyTime;
            currentJarTime = modifyTime;
        } else {
            log.info("dont find jar file...please check your hot update jar path");
        }
        log.info("is hot update is {}", isHotUpdate);
        return isHotUpdate;
    }
}
