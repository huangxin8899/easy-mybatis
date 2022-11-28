package cn.huangxin.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * DemoApp
 *
 * @author 黄鑫
 */
@SpringBootApplication
@ComponentScan("cn.huangxin.em")
public class DemoApp {

    public static void main(String[] args) {
        SpringApplication.run(DemoApp.class, args);
    }
}
