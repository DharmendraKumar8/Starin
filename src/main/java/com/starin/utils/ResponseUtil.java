package com.starin.utils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Overridden Method For generating Preparing 
 * Response
  */
public final class ResponseUtil {

	final static private Logger logger = LoggerFactory.getLogger(ResponseUtil.class);
    
	/**
     * Use This Method When Need to Success Response with Status Code 200
     * @param  data 
     * @param  message
     * @return ResponseEntity
     */
	
	final static public ResponseEntity<Object> response(Object data,String message){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("isSuccess",true);
		map.put("data",data);
		map.put("status",HttpStatus.OK);
		map.put("message", message);
		map.put("timestamp",new Date());
		map.put("fielderror",null);
		return new ResponseEntity<Object>(map,HttpStatus.OK);
	}
	/**
	 * Use this Method when need to create Success Response with status other
	 * Then status code 200 
	 * @param data
	 * @param message
	 * @param status
	 * @return ResponseEntity
	 */
	final static public ResponseEntity<Object> response(Object data,String message,HttpStatus status){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("isSuccess",true);
		map.put("data",data);
		map.put("status",status);
		map.put("message", message);
		map.put("timestamp",new Date());
		map.put("fielderror",null);
		return new ResponseEntity<Object>(map,status);
	}
	/**
	 * Use This Method when need to create error Response 
	 * @param message
	 * @param httpStatus
	 * @return ResponseEntity
	 */
	final static public ResponseEntity<Object> errorResponse(String message,HttpStatus httpStatus){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("isSuccess",false);
		map.put("data",null);
		map.put("status",httpStatus);
		map.put("message", message);
		map.put("timestamp",new Date());
		map.put("fielderror",null);
		return new ResponseEntity<Object>(map,httpStatus);
	} 
	/**
	 * Use This method when need to Genrating
	 * response relating to Spring's Form Validation
	 * Error
	 * @param message
	 * @param fieldError
	 * @return ResponseEntity
	 */
	
	final static public ResponseEntity<Object> fieldErrorResponse(String message,Object fieldError){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("isSuccess",false);
		map.put("data",null);
		map.put("status",HttpStatus.BAD_REQUEST);
		map.put("message", message);
		map.put("timestamp",new Date());
		map.put("fielderror",fieldError);
		return new ResponseEntity<Object>(map,HttpStatus.BAD_REQUEST);
	} 

	/**
	 * This method is used when we want to directly write content in response field manually
	 * @param data 
	 * @param status 
	 * @param error  
	 * @param message 
	 * @return 
	 */


	final static public void genrate(HttpServletResponse response,String errorMessage,int status){		
		ObjectMapper mapper = new ObjectMapper();
		Jackson2JsonObjectMapper jackson2JsonObjectMapper=new Jackson2JsonObjectMapper(mapper);
		logger.debug("Writing response manually using ResponseUtil's genrate method");
		Map<String,Object> responseMessage = new HashMap<String,Object>();		
		responseMessage.put("timestamp", new Date());		
		responseMessage.put("status", status);		
		responseMessage.put("isSuccess", false);	
		responseMessage.put("message",errorMessage);
		responseMessage.put("data",null);	
		responseMessage.put("fielderror",null);

		try {
			String json = jackson2JsonObjectMapper.toJson(responseMessage);

			response.setStatus(status);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding(StandardCharsets.UTF_8.toString());		
			response.getWriter().write(json);
		} catch (Exception e) {		
			e.printStackTrace();
		}			
	}
}