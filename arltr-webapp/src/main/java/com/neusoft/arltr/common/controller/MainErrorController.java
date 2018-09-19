/**
 * 
 */
package com.neusoft.arltr.common.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 错误处理控制器
 * 
 *
 *
 */
@Controller
public class MainErrorController implements ErrorController {

	@RequestMapping(value="/error")  
    public String handleError(){  
        return "error";  
    }

	@Override
	public String getErrorPath() {
		
		return "/error";
	}  
}
