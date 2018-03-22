package com.starin.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.starin.StarinApplication;
import com.starin.constants.KYCConstants;
import com.starin.domain.documents.form.KYCDocFormMeta;
import com.starin.domain.role.Activity;
import com.starin.enums.FieldType;

public class KYCUtilities {

	private static final Logger logger = LoggerFactory.getLogger(KYCUtilities.class);
	private static MessageDigest encoder=null;	
	private static BCryptPasswordEncoder encoder2=null;
	private static HashSet<String> acceptedTypeImage;
	private static String DOT = ".";

	/*
	 *  function for Loading Property file. 
	 */
	public static Properties propertiesFileReader(String filePath){

		Properties prop = null;
		InputStream input = null;
		try{
			input = StarinApplication.class.getClassLoader().getResourceAsStream(filePath);
			prop = new Properties();
			prop.load(input);
		}catch (Exception e) {
			logger.error("Exception in Properties file Reading : "+e.getMessage());
			e.printStackTrace();
			return null;
		}
		return prop;
	}
	/*
	 * Utility For Creating new Property File
	 */
	public static InputStream createNewPropertiesFile(String filePath){
		File file = new File(filePath);
		InputStream input = null;
		if(!file.exists()){
			try {
				file.createNewFile();
				input = new FileInputStream(filePath);
				logger.info("File : \""+filePath+"\" created succesfully.");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return input;
	}
	/*
	 * Utility For Dividing data in Batch Size
	 */
	public static List createBatch(List list,int batchSize){

		List<List> batchedList = new ArrayList<List>();
		if(list == null)
			return null;
		int numberOfbatch =(list.size() / batchSize);
		int firstIndex =0;

		for(int batchNum = 1;batchNum <= numberOfbatch;batchNum++){
			batchedList.add(list.subList(firstIndex,firstIndex+ batchSize));
			firstIndex = firstIndex+ batchSize;
		}

		if( list.size() % batchSize != 0 ){
			batchedList.add(list.subList(firstIndex,list.size()));
		}

		return batchedList;
	}

	/**
	 * <p>
	 * This method is used to create an object of class using reflection.
	 * </p>
	 * 
	 * @param className
	 *            Name of the class.
	 * @return Returns an object of specified class.
	 * @exception ClassNotFoundException
	 *                Thrown when an application tries to load in a class
	 *                through its string name.
	 * @see {@link Class#forName(String)}
	 * 
	 */
	public static Class<?> classForClassName(String className) {
		Class<?> object = null;
		try {
			object = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return object;
	}
	/*
	 * Utilty For Getting All Activity Object used in KYC System
	 */
	public static List<Activity> getAllActivityObject(String[] packageNames){
		List<Activity> activities = new ArrayList<Activity>();
		Set<String> mappingNames = new HashSet<>();
		for(String packageName : packageNames){
			for(String className : classNamesForPackage(packageName.trim())){
				Class<?> handlerClassObj = classForClassName(className);
				if(isNull(handlerClassObj))
					continue;
				Method[] methods = handlerClassObj.getDeclaredMethods();
				for (Method method : methods) {
					if (method.isAnnotationPresent(com.starin.utils.Activity.class) && Modifier.isPublic(method.getModifiers())) {
						if(!mappingNames.contains(method.getName())){
							activities.add(getActivityObject(method.getAnnotation(com.starin.utils.Activity.class),method.getName(),className));
							mappingNames.add(className+"_"+method.getName());
						}else{
							logger.info("Mapping Name ::::::: \""+className+"_"+method.getName()+"\" already exist.");
						}
					}
				}
			}
		}
		return activities;
	}

	/*
	 * Utility Function For Receiving Activity Object
	 * Corresponding to Handler Method Name  
	 */
	public static Activity getActivityObject(com.starin.utils.Activity annotaion,String handlerMethodName,String classNameWithPkg){
		Activity activity = new Activity( ( annotaion.activityName().equals("")) ? handlerMethodName : annotaion.activityName()  ,
				annotaion.activityDescription(),
				annotaion.active(),
				(annotaion.handlerMethodName().equals("")) ? classNameWithPkg+"_"+handlerMethodName : annotaion.handlerMethodName(),
						annotaion.url(),
						annotaion.methodType()
				);
		return activity;
	}
	/*
	 * Utility For Fetching All Classes Available in Package
	 */
	public static ArrayList<String> classNamesForPackage(String packageName) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		ArrayList<String> classNamesOfPackage = new ArrayList<String>();
		String packageNameOrig = packageName;
		packageName = packageName.replace(".", "/");
		URL packageURL = classLoader.getResource(packageName);
		if (!packageURL.getProtocol().equals("jar")) {
			URI uri = null;
			try {
				uri = new URI(packageURL.toString());
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			if (uri != null) {
				File folder = new File(uri);
				File[] contenuti = folder.listFiles();
				if (contenuti != null) {
					for (File actual : contenuti) {
						if (actual.isDirectory()) {
							classNamesOfPackage.addAll(classNamesForPackage(packageNameOrig + DOT + actual.getName()));
							continue;
						}
						String entryName = actual.getName();
						entryName = entryName.substring(0, entryName.lastIndexOf(DOT));
						classNamesOfPackage.add(packageNameOrig + DOT + entryName);
					}
				}
			}
		}else {
			try {
				URLConnection urlConnection = packageURL.openConnection();
				if (urlConnection instanceof JarURLConnection) {
					JarURLConnection connection = ((JarURLConnection) urlConnection);
					JarFile jarFile = connection.getJarFile();
					getClassesFromJarEntry(jarFile, packageName, classNamesOfPackage);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return classNamesOfPackage;
	}

	public static ArrayList<String> getClassesFromJarEntry(JarFile jarFile, String packageName, ArrayList<String> classNamesOfPackage) {
		Enumeration<JarEntry> enumeration = jarFile.entries();
		while (enumeration.hasMoreElements()) {
			JarEntry jarEntry = enumeration.nextElement();
			if ((!jarEntry.isDirectory()) && (jarEntry.getName().startsWith(packageName)) && (jarEntry.getName().endsWith(".class"))) {
				String className = jarEntry.getName().replaceAll("/", "\\.");
				classNamesOfPackage.add(className.substring(0, className.length() - 6));
			}
		}
		return classNamesOfPackage;
	}

	public static boolean isNull(Object obj){
		return (obj == null)? true : false;
	}

	/**
	 * Utility For getting Map of All Validation Related Error
     Occur in Spring's Form Validation procedure
	 * @param result
	 * @return filederror 
	 */
	public static Map<String,Object> getFieldErrorResponse(BindingResult result){	
		Map<String,Object> fielderror=new HashMap<String,Object>();	
		List<FieldError> errors =result.getFieldErrors();		
		for(FieldError error : errors){		 
			fielderror.put(error.getField(), error.getDefaultMessage());		
		}		return fielderror;
	}

	/**
	 * MD5 Encoder
	 * @param plaintext
	 * @return ciphertext
	 */
	public static String encoder(String plaintext){	
		logger.info("inside encoder");	
		if(encoder==null){		
			try {		
				encoder=MessageDigest.getInstance("MD5");	
			} catch (NoSuchAlgorithmException e) {	
				e.printStackTrace();		}	 
		}	 		  encoder.update(plaintext.getBytes());		
		byte byteData[] = encoder.digest();		
		StringBuffer ciphertext = new StringBuffer();	
		for (int i = 0; i < byteData.length; i++) {	
			ciphertext.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));	
		}		  logger.info(" encoded plaintext : "+ciphertext.toString());	
		return ciphertext.toString();	  
	}	

	/**
	 * Utility to find Whether Passed Activity 
	 * is Public or private
	 * @param activity
	 * @return
	 */
	public static boolean isPublicActivity(String activity){	
		Set<String> methods=new HashSet<String>();

		//public APIs in KYC
		methods.add("signUp");
		methods.add("login");
		methods.add("reset");
		methods.add("updatePassword");
		methods.add("verify");
		methods.add("logout");
		methods.add("getAllCountry");
		//end of public APIs in KYC

		//swagger related configuration
		methods.add("securityConfiguration");
		methods.add("swaggerResources");
		methods.add("getDocumentation");
		methods.add("uiConfiguration");
		//end of swagger related configuration end

		//error related configuration
		methods.add("errorHtml");//this is default default html genrated by spring so for the time being it is public
		methods.add("error");
		//end of error related configuration

		//These allowing API for miners for all roles
		methods.add("walletUserAccountsStatus");
		methods.add("getUserWithNoWallet");
		methods.add("postUserWithNoWallet");
		//end of API for merchant roles


		return methods.contains(activity);
	}	
	/**
	 * Bcrypt Encoder for secruing password
	 * Offers one to many hash
	 * @param password
	 * @return
	 */
	public static String bcryptEncryptor(String password){
		if(encoder2==null){
			logger.info("Creating instance of Bcrypt encoder");
			encoder2=new BCryptPasswordEncoder();	
			logger.info("sample crypt  sample ="+encoder2.encode("sample"));
		}		
		return encoder2.encode(password);	
	}

	/**
	 * Utility Method used to match raw Plaintext
	 * and encrypted Bcrypt ciphertext
	 * @param rawplaintext
	 * @param ciphertext
	 * @return
	 */
	public static boolean bcryptEncryptorMatch(String rawplaintext,String ciphertext){
		if(encoder2==null){
			logger.info("Creating instance of Bcrypt encoder");
			encoder2=new BCryptPasswordEncoder();	
			logger.info("sample crypt  sample ="+encoder2.encode("sample"));
		}	
		return encoder2.matches(rawplaintext, ciphertext);
	}

	public static <T> List<T> loadCSVFile(Class<T> type,InputStream inputStream) {
		try {
			CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
			CsvMapper mapper = new CsvMapper();
			MappingIterator<T> readValues = 
					mapper.reader(type).with(bootstrapSchema).readValues(inputStream);
			return readValues.readAll();
		} catch (Exception e) {
			logger.error("Error occurred while loading object list from file " + inputStream, e);
			return Collections.emptyList();
		}
	}

	public static boolean isDocumentFormatAccepted(String format,String acceptedImage[]){		
		if(acceptedTypeImage==null){		
			acceptedTypeImage=new HashSet();	
			for(String image:acceptedImage){	
				acceptedTypeImage.add(image.toLowerCase());		
			}	
		}	
		return acceptedTypeImage.contains(format.toLowerCase());
	}

	public static String getFileNameExtension(String fileName){

		if(fileName == null){
			return null;
		}
		int lastindex=fileName.lastIndexOf(".");

		if(lastindex < 0)
			return null;

		return fileName.substring(lastindex+1,fileName.length());
	}

	public static <E> List<E> getListA_Minus_ListBObjects(List<E> list1,List<E> list2,String comparisionFieldName){
		List<E> uniquesObjArray = new ArrayList<E>();
		//	if(list1.size() > list2.size()){
		for(E element1 : list1){
			try {
				Field field1 = element1.getClass().getDeclaredField(comparisionFieldName);
				field1.setAccessible(true);
				Object element1Value =field1.get(element1);
				boolean flag = false;
				for(E element2 :  list2){
					Field field2  = element2.getClass().getDeclaredField(comparisionFieldName);
					field2.setAccessible(true);
					Object element2Value = field2.get(element2);
					if(element1Value.equals(element2Value)){
						flag = true;
						break;
					} 	
				}
				if(!flag)
					uniquesObjArray.add(element1);

			} catch (NoSuchFieldException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}

		}
		//}
		return uniquesObjArray;
	}

	public static <E> List<E> getListA_ListB_Intersection(List<E> list1,List<E> list2,String comparisionFieldName){

		List<E> uniquesObjArray = new ArrayList<E>();
		uniquesObjArray = getListA_Minus_ListBObjects(list1, list2, comparisionFieldName);
		List<E> tmpObjArray = getListA_Minus_ListBObjects(list2, list1, comparisionFieldName);
		for(E e : tmpObjArray)
			uniquesObjArray.add(e);	
		return uniquesObjArray;
	}

	public static boolean testPattern(String content,String inputpattern){
		Pattern pattern = Pattern.compile(inputpattern);
		Matcher matcher = pattern.matcher(content);
		return matcher.matches();
	}

	public static boolean isJSONString(String jsonString){
		try{
			new JSONObject(jsonString);
			return true;
		}catch (Exception e) {
			return false;
		}
	} 

	public static String jsonString(Map<String,Object> jsonMap){
		try{
			return new JSONObject(jsonMap).toString();
		}catch (Exception e) {
			return null;
		}
	} 

	public static Map<String,Object> CollectionToMapJson(String key,Collection<KYCDocFormMeta> objects,String fieldtoinclude){
		Map<String,Object> result=new HashMap<String,Object>();
		if(objects==null)
			return null;
		Iterator<KYCDocFormMeta> iterator=objects.iterator();
		while(iterator.hasNext()){
			KYCDocFormMeta instance=iterator.next();
			result.put(instance.getField().getName(), ObjectMap.objectMap(instance,fieldtoinclude));
		}
		return result;
	}

	public static Map<String,Object> _successMultipleObject(Object data,String message){
		Map<String,Object> result=new HashMap<String,Object>();
		result.put(KYCConstants.data_literal, data);
		result.put(KYCConstants.error_literal, false);
		result.put(KYCConstants.message_literal,message);
		return result;
	}

	public static Map<String,Object> _errorMultipleObject(String message,HttpStatus statusCode){
		Map<String,Object> result=new HashMap<String,Object>();
		result.put(KYCConstants.data_literal, null);
		result.put(KYCConstants.error_literal, true);
		result.put(KYCConstants.message_literal,message);
		result.put(KYCConstants.http_status_literal,statusCode);
		return result;
	}

	public static boolean isTypeSupported(String type){
		try{
			FieldType enumtype=FieldType.valueOf(type);
			return true;
		}catch(Exception e){
			logger.debug("type no supported ",e);
			return false;
		}
	}

	public static Map<String,Object> errorResponseMap(String message,HttpStatus statusCode){
		Map<String,Object> error =new HashMap<String,Object>();
		error.put(KYCConstants.error_literal, true);
		error.put(KYCConstants.message_literal,message);
		error.put(KYCConstants.http_status_literal,statusCode);
		return error;
	} 

	public static void checkRegex(String argument)  throws PatternSyntaxException{
		Pattern.compile(argument);
	}

	public static boolean isInteger(String value){
		if(value == null)
			return false;
		Integer integer = null;
		try{
			integer = Integer.parseInt(value);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	public static Integer parseIntoInteger(String value){
		if(value == null)
			return null;
		try{
			return Integer.parseInt(value);
		}catch(Exception e){
			logger.debug("Not a integer exception",e);
			return null;
		}
	}
	public static Long parseToLong(String value){

		try{
			Long documentsharingid=Long.parseLong(value);
			return documentsharingid;
		}catch(Exception e){
			return null;
		}
	}

	public static Boolean parseIntoBoolean(String value){
		if(value == null)
			return null;
		try{
			return Boolean.parseBoolean(value);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public  static Boolean parseIntoBooleanValue(String value) throws Exception{
		if(value==null)
			throw new NullPointerException("value passed is null");
		else if(value.equalsIgnoreCase("true"))
			return true;
		else if(value.equalsIgnoreCase("false"))
			return false;
		else
			throw new Exception("value is not of boolean type,can't cast it");
	}

	public static JSONArray getJSONArray(String jsonString){
		try{
			return new JSONArray(jsonString);
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static JSONObject getJSONObject(String jsonString){		
		try{			
			return new JSONObject(jsonString);		
			}catch (Exception e) {			
				logger.error("Exception occur while create JSON Object from json string ");			
	     return null;
	    }	
		}
}