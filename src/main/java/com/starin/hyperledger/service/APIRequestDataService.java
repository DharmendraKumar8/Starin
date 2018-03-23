package com.starin.hyperledger.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.starin.exceptions.KYCException;

@Service
public class APIRequestDataService {

	private static final Logger logger = LoggerFactory.getLogger(APIRequestDataService.class);

	public APIRequestDataService() {
	}

	@Autowired
	private HyperledgerApi hyperledgerApi;

	public static String encodeDocument(byte[] byteImage) {
		return Base64.encodeBase64URLSafeString(byteImage);
	}

	public static byte[] decodeDocument(String binaryDocumentString) {
		return Base64.decodeBase64(binaryDocumentString);
	}

	public String getIncrementedDocumentId(String docId) {

		if (docId == null)
			return null;

		Integer docNumber = Integer.parseInt(docId.substring(1));
		logger.info("documnetId number for increment is : " + docNumber);
		docNumber = docNumber + 1;
		logger.info("increment is documnetId is  : m" + docNumber);
		return "m" + docNumber;
	}

	public String getIncrementedOwnerId(String ownerId) {

		if (ownerId == null)
			return null;

		Integer ownNumber = Integer.parseInt(ownerId.substring(1));
		logger.info("ownNumber number for increment is : " + ownNumber);
		ownNumber = ownNumber + 1;
		logger.info("increment is ownerId is  : o" + ownNumber);
		return "o" + ownNumber;
	}

	public Map<String, Object> login(String uuid, String secret) {
		return hyperledgerApi.login(uuid, secret);
	}

	/**
	 * 
	 * @param file
	 *            multiPart file to be uploaded
	 * @param request
	 *            it is assumed that calling method is setting user,filename and
	 *            channel parameter in the request map if not set service will throw
	 *            exception
	 * @param meta
	 *            metaData related to document
	 * @param hyperLedger_token
	 *            for user authentication over hyper ledger
	 * @return response check for success flag in the return map
	 * @throws KYCException
	 *             if some thing wrong happen it will throw KYCException
	 */
	public Map<String, Object> uploadDocument(MultipartFile file, Map<String, Object> request, Map<String, Object> meta,
			String hyperLedger_token) throws KYCException {
		try {
			byte[] imageByte = file.getBytes();
			String encodedDocument = encodeDocument(imageByte);
			logger.debug("encode string of document " + encodedDocument);
			Map<String, Object> document = new HashMap<String, Object>();
			// document.put("filename", file.getName()+UUID.randomUUID().toString());
			document.put("data", encodedDocument);
			document.put("meta", meta);
			request.put("document", document);
			request = hyperledgerApi.upload(request, hyperLedger_token);
			return request;
		} catch (IOException e) {
			Map<String, Object> res = new HashMap<String, Object>();
			res.put("success", false);
			res.put("message", "not able to upload document,please try again later");
			res.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
			return res;

		}
	}

	// Method for create JSON request for hyperledger user creation
	public JSONObject createJSONRequestForUserCreation(String uuid) {

		if (uuid == null) {
			return null;
		}
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("uuid", uuid);
		} catch (JSONException e) {
			logger.error("Exception occur while create json request structure for user creation on hyperledger");
			return null;
		}
		return requestJson;
	}
}
