package com.bootstrap.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller 
public class GlobalDefaultExceptionHandler implements ErrorController{  
	private final static String ERROR_PATH = "/error";
	@Override
	@RequestMapping(value = ERROR_PATH, produces = "text/html")
	public String getErrorPath() {
		//只要有错误就跳转到此页面，但是url不变
		//return "MonthBill/billList";
		return "MonthBill/login";
	}  
} 
