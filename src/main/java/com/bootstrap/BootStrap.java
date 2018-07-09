package com.bootstrap;
/**
 *  Author:zouyang
 * 	Data:
 * 
 */


import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BootStrap {
		
	//点击确认按钮，进入bootstrapLocation.html页面
	@RequestMapping("bootstrapindex")
	public String test(Model model,HttpServletRequest request, HttpServletResponse response) throws UnknownHostException{
		System.out.println("进入bootstrap");
		String addr = InetAddress.getLocalHost().getHostAddress();//获得本机IP
		System.out.println("本机ip:"+addr);
		return "bootstrap";
	}
	
	//点击确认按钮，进入bootstrapLocation.html页面
	@RequestMapping("bootstrapLocation")
	public String location(Model model,HttpServletRequest request, HttpServletResponse response){
		System.out.println("进入本地bootstrap");
		return "bootstrapLocation";
	}
	
	//左侧导航栏
	@RequestMapping("bootstrapLeft")
	public String bootstrapLeft(Model model,HttpServletRequest request, HttpServletResponse response){
		System.out.println("进入本地bootstrap");
		return "leftNavi";
	}
	
	//提交模板页面
	@RequestMapping("bootstrapForms")
	public String bootstrapForms(Model model,HttpServletRequest request, HttpServletResponse response){
		System.out.println("进入提交模板bootstrapForms");
		return "forms";
	}

}
