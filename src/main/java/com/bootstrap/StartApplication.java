package com.bootstrap;

/**
 *  Author:zouyang
 * 	Data:2018-4-3
 * 	项目启动类
 */


import org.mybatis.spring.annotation.MapperScan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan("com.bootstrap.mybatis")
public class StartApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(StartApplication.class,args);
		//查看项目已经启动
		System.out.println("monthbill project start...");
	}
	
}
