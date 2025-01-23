package com.david.apis.loancalc.service;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.david.apis.loancalc.model.AppRuntimeException;
import com.david.apis.loancalc.model.GenericErrorResponse;

/**
 * 	Handle general exceptions throw by the application so that we can respond accordingly to the client.
 * 
 * 	For example, if the API threw an exception after getting a null object, we can respond to the client with 
 * 	{ "errorCode": "103", "description": "internal error occurred" }
 *  
 */
// @RestControllerAdvice
public class RESTAdvisor {
	private static Logger LOG = LoggerFactory.getLogger(RESTAdvisor.class);
	
	@ResponseStatus(INTERNAL_SERVER_ERROR)
	@ExceptionHandler(AppRuntimeException.class)
	public @ResponseBody GenericErrorResponse handleUnknownException(ServerHttpRequest request, AppRuntimeException ex) {
		return createGenericHttpErrorInfo(INTERNAL_SERVER_ERROR, request, ex);
	}
	
	private GenericErrorResponse createGenericHttpErrorInfo(HttpStatus httpStatus, ServerHttpRequest request, Exception ex) {
		
		final String path = request.getPath().pathWithinApplication().value();
		final String message = ex.getMessage();
		
		LOG.debug("Returning HTTP status: {} for path: {}, message: {}", httpStatus, path, message);
		
		return new GenericErrorResponse(path, httpStatus.value(), message);
	}
}
