package com.moz.ates.traffic.admin.common.exception;

import com.moz.ates.traffic.common.support.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestControllerAdvice
public class AdminExceptionHandler extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(value = Exception.class)
	public ModelAndView exceptionHandler(Exception e){
		e.printStackTrace();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("views/common/ErrorPage");
		mav.addObject("message", e.getMessage());
		return mav;
	}
	
	// LoginCheckException
	@ExceptionHandler(value = NoLoginException.class)
	public ModelAndView loginChkExceptionHandler(NoLoginException ne) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("views/main/login");
		return mav;
	}
	
	// customRuntimeException 
	@ExceptionHandler(value = CommonException.class)
	public ResponseEntity<ErrorResponse> adminExceptionHandler(CommonException ce, String message) {
		ErrorResponse response = new ErrorResponse(ce.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ce.getErrorCode().getStatus()));
	}

	@ExceptionHandler(value = CommonResponseException.class)
	public ModelAndView customErrorExceptionHandler(CommonResponseException e){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("views/common/ErrorPage");
		mav.addObject("message", e.getMessage());
		return mav;
	}

	@ExceptionHandler(value = NotPermissionException.class)
	public ModelAndView notPermissionExceptionHandler(NotPermissionException e){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("views/common/ErrorPage");
		// 권한이 없습니다. 관리자에게 문의하세요.
		mav.addObject("message", "Você não tem permissão. Por favor, pergunte ao seu administrador.");
		return mav;
	}
}
