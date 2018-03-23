package com.starin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

import com.starin.conf.EnvConfiguration;
import com.starin.constants.KYCConstants;
import com.starin.constants.RequestMethod;
import com.starin.domain.user.LoginCredential;
import com.starin.domain.user.RegisterUser;
import com.starin.domain.user.RegistrationCredential;
import com.starin.domain.user.User;
import com.starin.exceptions.CountryNotFoundException;
import com.starin.exceptions.RoleNotFoundException;
import com.starin.exceptions.UserAlreadyExistException;
import com.starin.service.MailService;
import com.starin.service.token.AuthTokenService;
import com.starin.service.user.UserService;
import com.starin.utils.Activity;
import com.starin.utils.KYCUtilities;
import com.starin.utils.MessageUtility;
import com.starin.utils.ResponseUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Auth_Controller", description = "Operation pertaining to user authentication")
@RestController
@RequestMapping("/api/v1")
public class AuthController {
	@Autowired
	private UserService userservice;

	@Autowired
	private AuthTokenService authtokenservice;

	@Autowired
	private EnvConfiguration evnConfiguration;

	@Autowired
	private MailService mailservice;
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Activity(methodType = RequestMethod.POST, url = "api/v1/signup")
	@ApiOperation(value = "Activity for user registration", response = ResponseEntity.class)
	@PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> signUp(@Valid @RequestBody RegistrationCredential credential, BindingResult result) {
		logger.info("Signup in progress");
		if (result.hasErrors())
			return ResponseUtil.fieldErrorResponse(MessageUtility.getErrorMessage("FieldError"),
					KYCUtilities.getFieldErrorResponse(result));
		else {
			if (!credential.getType().toLowerCase().matches(evnConfiguration.getValidRoleName()))
				return ResponseUtil.errorResponse(MessageUtility.getErrorMessage("NotValidRoleNameRegistration"),
						HttpStatus.BAD_REQUEST);
		}
		try {
			Map<String, Object> res = userservice.registerUser(credential);
			Context context = new Context();
			context.setVariable("username", res.get("name"));
			context.setVariable("verificationLink", evnConfiguration.getFrontEndUrl() + "?token=" + res.get("token"));
			RegisterUser user = new RegisterUser();
			user.setEmail((String) res.get("email"));
			mailservice.sendScheduleHtmlMail(user, "Verification link", context, KYCConstants.verificationTemplateName);
		} catch (CountryNotFoundException countryExp) {
			return ResponseUtil.errorResponse(MessageUtility.getErrorMessage("CountryNotFound"),
					HttpStatus.BAD_REQUEST);
		} catch (RoleNotFoundException e1) {
			return ResponseUtil.errorResponse(MessageUtility.getErrorMessage("RoleNotFound"), HttpStatus.BAD_REQUEST);
		} catch (UserAlreadyExistException e1) {
			return ResponseUtil.errorResponse(MessageUtility.getErrorMessage("DataIntegrityMessage"),
					HttpStatus.CONFLICT);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			return ResponseUtil.errorResponse(MessageUtility.getErrorMessage("DataIntegrityMessage"),
					HttpStatus.CONFLICT);
		} catch (Exception e) {
			logger.error("****************Error Crtical***************please look into ", e);
			return ResponseUtil.errorResponse(MessageUtility.getErrorMessage("IntervalServerError"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("username", credential.getEmail());
		return ResponseUtil.response(data, MessageUtility.getSuccessMessage("RegistrationSucess"));
	}

	@Activity(methodType = RequestMethod.POST, url = "api/v1/login")
	@ApiOperation(value = "Activity for user login", response = ResponseEntity.class)
	@PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> login(@Valid @RequestBody LoginCredential credential, BindingResult result,
			HttpServletRequest request) {
		if (result.hasErrors())
			return ResponseUtil.fieldErrorResponse(MessageUtility.getErrorMessage("FieldError"),
					KYCUtilities.getFieldErrorResponse(result));
		User user = userservice.findByEmail(credential.getEmail().trim());
		logger.info("user is  : " + user);
		if (user == null) {
			logger.debug("user is null");
			return ResponseUtil.errorResponse(MessageUtility.getErrorMessage("UserNotFound"), HttpStatus.UNAUTHORIZED);
		} else if (!KYCUtilities.bcryptEncryptorMatch(credential.getPassword(), user.getPassword())) {
			logger.info("wrong password");
			return ResponseUtil.errorResponse(MessageUtility.getErrorMessage("PasswordNotExist"),
					HttpStatus.UNAUTHORIZED);
		} else {
			Map<String, Object> data = null;
			try {
				data = userservice.getLoginToken(user, request);
			} catch (Exception e) {
				logger.debug("Critical ***************************** Login down****************", e);
				logger.debug("Exception", e);
				return ResponseUtil.errorResponse(MessageUtility.getErrorMessage("IntervalServerError"),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return ResponseUtil.response(data,
					MessageUtility.getSuccessMessage("loginNotification") + " " + user.getUsername());
		}
	}

	@Activity(methodType = RequestMethod.DELETE, url = "api/v1/logout")
	@ApiOperation(value = "Activity for user logout", response = ResponseEntity.class)
	@DeleteMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> logout(
			@RequestHeader(name = KYCConstants.KYC_Token_Header, defaultValue = "") String authToken) {
		int status = authtokenservice.logout(authToken);
		if (status == -1)
			return ResponseUtil.errorResponse(MessageUtility.getErrorMessage("UnknownUser"), HttpStatus.BAD_REQUEST);
		else
			return ResponseUtil.response(null, MessageUtility.getSuccessMessage("loggedout"));
	}
}
