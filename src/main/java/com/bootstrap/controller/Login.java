package com.bootstrap.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bootstrap.dao.MonthBillDao;
import com.bootstrap.entity.Monthbill;
import com.bootstrap.mybatis.MonthBillMybatisDao;

/**
 *  Author:zouyang
 * 	Data:2018-4-12
 *  用户登录
 *  
 */

@Controller
public class Login {

	MonthBillDao monthBillDao = new MonthBillDao();
	
	//跳转进入登录页面
	@RequestMapping("login")
	public String getLoginHtml(Model model,HttpServletRequest request,HttpServletResponse response,String username,String pwd){
		//进入登录页面	    
		return "MonthBill/login";
	}

	//校验用户名和密码
	@RequestMapping("checkUser")
	@ResponseBody
	public int UserLogin(Model model,HttpServletRequest request,HttpServletResponse response,String username,String pwd){
		//用户登录,如果verify等于1，则表示用户名和密码正确
		int verify = 0;
		if(username.equals("sfadmin")&&pwd.equals("sfadmin123456")){
			verify= 1;
			request.getSession().setAttribute("username", "sfadmin");
		}
		return verify;
	}
	
	//获取session中的登录名
	@RequestMapping("getUser")
	@ResponseBody
	public String getUser(Model model,HttpServletRequest request,HttpServletResponse response){
		return (String) request.getSession().getAttribute("username");
	}
	
	//退出清空session中的用户名
	@RequestMapping("loginOut")
	public void loginOut(Model model,HttpServletRequest request,HttpServletResponse response){
		request.getSession().setAttribute("username", "");
	}
}
