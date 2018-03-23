package com.starin.hyperledger.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.starin.conf.EnvConfiguration;
import com.starin.utils.KYCUtilities;

@Service
public class HyperledgerApi {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private EnvConfiguration envConfiguration;

	private static final Logger logger = LoggerFactory.getLogger(HyperledgerApi.class);

	public HyperledgerApi() {
	}

	public Map<String, Object> login(String uuid, String secret) {
		String url = "http://" + envConfiguration.getHyperLedgerIp() + ":" + envConfiguration.getHyperLedgerPort()
				+ envConfiguration.getHUserTokenApi();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("uuid", uuid);
		map.put("secret", secret);
		HttpEntity<Map<String, Object>> request = new HttpEntity<Map<String, Object>>(map, headers);
		ResponseEntity<String> response = null;
		Map<String, Object> res = new HashMap<String, Object>();
		JSONObject jsonResponse;
		try {
			response = restTemplate.postForEntity(url, request, String.class);
		} catch (HttpClientErrorException e) {
			logger.error("httpClientErrorException while sending request ", e);
			res.put("success", false);
			res.put("status", e.getStatusCode());
			res.put("message", "can not login into BKVSDM");
			return res;
		} catch (HttpServerErrorException e) {
			logger.debug("server responded with 5xx status code", e);
			res.put("success", false);
			res.put("status", e.getStatusCode());

			try {
				jsonResponse = new JSONObject(e.getResponseBodyAsString());
				res.put("message", jsonResponse.getString("message"));
			} catch (JSONException e1) {
				logger.debug("error parsing HttpServerException json response", e1);
				res.put("message", "can not login into BKVSDM");
			}
			return res;
		} catch (Exception e) {
			logger.debug("exception while sending request to hyperledger", e);
			res.put("success", false);
			res.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
			res.put("message", "can not login into BKVSDM");
			return res;
		}

		try {
			if (response == null) {
				res.put("success", false);
				res.put("status", HttpStatus.BAD_GATEWAY);
				res.put("message", "can not login into BKVSDM");
				return res;
			}
			jsonResponse = new JSONObject(response.getBody());
			response = null;
			boolean success = jsonResponse.getBoolean("success");
			res.put("success", success);
			if (success) {
				res.put("data", jsonResponse.getJSONObject("data"));
				res.put("message", "Successfully logged in into BKVSDM");
				return res;
			} else {
				res.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
				res.put("message", "can not login into BKVSDM");
				return res;
			}
		} catch (JSONException e) {
			logger.debug("Exception while parsing response from server", e);
			res.put("success", false);
			res.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
			res.put("message", "can not login into BKVSDM");
			return res;
		}
	}

	public Map<String, Object> upload(Map<String, Object> request, String hyperLedger_token) {
		String url = "http://" + envConfiguration.getHyperLedgerIp() + ":" + envConfiguration.getHyperLedgerPort()
				+ envConfiguration.getHDocumentUploadApi();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", hyperLedger_token);
		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(request, headers);
		ResponseEntity<String> response = null;
		Map<String, Object> res = new HashMap<String, Object>();
		JSONObject jsonResponse;
		try {
			response = restTemplate.postForEntity(url, requestEntity, String.class);
		} catch (HttpClientErrorException e) {
			logger.error("httpClientErrorException while sending request ", e);
			res.put("success", false);
			res.put("status", e.getStatusCode());
			res.put("message", "not able to upload document,please try again later");
			return res;
		} catch (HttpServerErrorException e) {
			logger.debug("server responded with 5xx status code", e);
			res.put("success", false);
			res.put("status", e.getStatusCode());

			try {
				System.out.println("++++++++++++++++++++++++" + e.getResponseBodyAsString());
				jsonResponse = new JSONObject(e.getResponseBodyAsString());
				res.put("message", jsonResponse.getString("message"));
			} catch (JSONException e1) {
				logger.debug("error parsing HttpServerException json response", e1);
				res.put("message", "not able to upload document,please try again later");
			}
			return res;
		} catch (Exception e) {
			logger.debug("exception while sending request to hyperledger", e);
			res.put("success", false);
			res.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
			res.put("message", "not able to upload document,please try again later");
			return res;
		}
		try {
			if (response == null) {
				res.put("success", false);
				res.put("status", HttpStatus.BAD_GATEWAY);
				res.put("message", "not able to upload document,please try again later");
				return res;
			}
			jsonResponse = new JSONObject(response.getBody());
			response = null;
			boolean success = jsonResponse.getBoolean("success");
			res.put("success", success);
			if (success) {
				res.put("data", jsonResponse.getJSONObject("data"));
				res.put("message", "Document uploaded successfully");
				return res;
			} else {
				res.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
				res.put("message", "can not login into BKVSDM");
				return res;
			}
		} catch (JSONException e) {
			logger.debug("Exception while parsing response from server", e);
			res.put("success", false);
			res.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
			res.put("message", "not able to upload document,please try again later");
			return res;
		}
	}

	public JSONObject createUser(JSONObject jsonRequest) {

		String postUrl = "http://" + envConfiguration.getHyperLedgerIp() + ":" + envConfiguration.getHyperLedgerPort()
				+ envConfiguration.getHUserRegisterApi();

		logger.info("Request Data for register user on hyperledger : " + jsonRequest);
		logger.info("Request Url for register user on hyperldger : " + postUrl);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Accept", "application/json");

		HttpEntity<String> httpEntity = new HttpEntity<String>(jsonRequest.toString(), httpHeaders);
		ResponseEntity<String> postResponse = null;
		logger.info("Request headers is : " + httpEntity.getHeaders());
		try {
			postResponse = restTemplate.exchange(postUrl, HttpMethod.POST, httpEntity, String.class);
			logger.info("Response of create User : " + postResponse.getBody());

			JSONObject responseJson = KYCUtilities.getJSONObject(postResponse.getBody());
			if (responseJson == null) {
				logger.error("Incorrect response getting from hyperledger");
				return null;
			}
			if (!responseJson.getBoolean("success")) {
				logger.error("get error response from hyperledger. message is " + responseJson.getString("message"));
				return null;
			}
			JSONObject jsonObject = responseJson.getJSONObject("data");
			return jsonObject;
		} catch (Exception e) {
			logger.error("Exception occur while create new user on hyperledger : " + e);
			return null;
		}

	}
}
