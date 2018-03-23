package com.starin.utils;

import java.util.Properties;

import com.starin.constants.KYCConstants;


/**
 * Message Utilty Class for reading
 * SuccessMessage and ErrorMessage
  */

public class MessageUtility {
	
	private static Properties successMessage =new Properties();
	
	private static Properties errorMesssage =new Properties();
	
	
	public MessageUtility(){}
	
	/**
	 * Initialize Success and ErrorMessage from
	 * Properties File	
	 */
	
	public void initialze(){
		loadSuccessMessage();
		loadErrorMessage();
	}
	/**
	 * Return value corresponding to successkey 
	 * if not found return null
	 * @param successkey
	 * @return
	 */
	public static String getSuccessMessage(String successkey){
		return (String)successMessage.get(successkey);
	}
	
	/**
	 * Return value corresponding to errorkey 
	 * if not found return null
	 * @param errorkey
	 * @return
	 */
	
	public static String getErrorMessage(String errorkey){
		return (String)errorMesssage.get(errorkey);
	}
	/**
	 * Loading success Messages
	 */
	private void loadSuccessMessage(){
		Properties propertiesObj = null;
		propertiesObj = KYCUtilities.propertiesFileReader(KYCConstants.successMessagePath);
		if(!KYCUtilities.isNull(propertiesObj)){
			successMessage = propertiesObj;
		}
	}
	
    /**
     * 
	 * Loading error Messages
	 */	
	private void loadErrorMessage(){
		Properties propertiesObj = null;
		propertiesObj = KYCUtilities.propertiesFileReader(KYCConstants.errorMessagePath);
		if(!KYCUtilities.isNull(propertiesObj)){
			errorMesssage = propertiesObj;
		}
	}
}
