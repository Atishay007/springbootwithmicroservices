package com.spring.microservices.exception.controllers;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * This is the customized exception controller.
 * 
 * @author Champ
 *
 */

@ControllerAdvice // so that it will eligible for all controllers.
@RestController // becoz it returns response
public class CustomizedResponsedEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class) // this is the exception thrown from one of the methods present in controller.
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) throws Exception {
		ExceptionResponse exResponse = new ExceptionResponse(new Date(), ex.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<Object>(exResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}