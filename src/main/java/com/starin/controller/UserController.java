package com.starin.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;

import com.starin.conf.EnvConfiguration;
import com.starin.constants.KYCConstants;
import com.starin.domain.AuthenticationToken;
import com.starin.domain.ResetToken;
import com.starin.domain.VerificationToken;
import com.starin.domain.documents.Country;
import com.starin.domain.role.SuperUserRole;
import com.starin.domain.user.ChangePasswordCredential;
import com.starin.domain.user.Email;
import com.starin.domain.user.Password;
import com.starin.domain.user.Req_ProfileUpdate;
import com.starin.domain.user.User;
import com.starin.exceptions.PasswordMismatchException;
import com.starin.service.CountryService;
import com.starin.service.MailService;
import com.starin.service.token.AuthTokenService;
import com.starin.service.token.ResetTokenService;
import com.starin.service.token.TokenService;
import com.starin.service.token.VerificationTokenService;
import com.starin.service.user.LoginHistoryService;
import com.starin.service.user.UserCountryService;
import com.starin.service.user.UserService;
import com.starin.utils.Activity;
import com.starin.utils.KYCDateUtil;
import com.starin.utils.KYCUtilities;
import com.starin.utils.MessageUtility;
import com.starin.utils.ObjectMap;
import com.starin.utils.ResponseUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "user-controller", description = "Operation pertaining to user")
@RestController
@RequestMapping("/api/v1")
public class UserController {

	@Autowired
	private UserService userservice;
	@Autowired
	private VerificationTokenService verificationservice;

	@Autowired
	private CountryService countryService;

	@Autowired
	private UserCountryService usercountryservice;

	@Autowired
	private TokenService tokenservice;

	@Autowired
	private MailService mailservice;
	@Autowired
	private AuthTokenService authtokenservice;
	@Autowired
	private EnvConfiguration configuration;
	@Autowired
	private ResetTokenService resetTokenService;
	@Autowired
	private LoginHistoryService loginHistoryService;

	@Autowired
	private SuperUserRole superuser;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	/**
	 * @param email
	 * @param result
	 * @param request
	 * @return
	 */
	@Activity(methodType = com.starin.constants.RequestMethod.PATCH, url = "api/v1/users/email/reset")
	@ApiOperation(value = "Activity for password reset", response = ResponseEntity.class)
	@PatchMapping(value = "users/email/reset", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> reset(@Valid @RequestBody Email email, BindingResult result,
			HttpServletRequest request) {
		if (result.hasErrors())
			return ResponseUtil.fieldErrorResponse(MessageUtility.getErrorMessage("FieldError"),
					KYCUtilities.getFieldErrorResponse(result));

		else {
			User user = userservice.findByEmail(email.getEmail().trim());

			if (user == null)
				return ResponseUtil.errorResponse(MessageUtility.getErrorMessage("UserNotFound"),
						HttpStatus.UNAUTHORIZED);
			else {
				resetTokenService.deleteOldTokens(user.getUid());
				ResetToken reset = new ResetToken();
				Date time = new Date();
				reset.setCreationDate(time);
				reset.setUser(user);
				reset.setToken(tokenservice.genrate().trim());
				resetTokenService.save(reset);
				Context context = new Context();
				context.setVariable("username", user.getName());
				context.setVariable("resetLink", configuration.getForgotLink() + "?token=" + reset.getToken());
				logger.debug("sync mail send in progress");
				mailservice.sendScheduleHtmlMail(user, "Password Reset link", context, KYCConstants.resetTemplate);
				return ResponseUtil.response(null, MessageUtility.getSuccessMessage("resetNotification"));
			}
		}
	}

	/**
	 * 
	 * @param password
	 * @param result
	 * @param token
	 * @return
	 */
	@Activity(methodType = com.starin.constants.RequestMethod.PATCH, url = "api/v1/users/reset/password")
	@ApiOperation(value = "Activity to update user's password ", response = ResponseEntity.class)
	@PatchMapping(value = "users/reset/password", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> updatePassword(@Valid @RequestBody Password password, BindingResult result,
			@RequestParam(required = false) String token) {
		if (result.hasErrors())
			return ResponseUtil.fieldErrorResponse(MessageUtility.getErrorMessage("FieldError"),
					KYCUtilities.getFieldErrorResponse(result));
		if (token == null)
			return ResponseUtil.errorResponse(MessageUtility.getErrorMessage("EmptyToken"), HttpStatus.UNAUTHORIZED);
		ResetToken rtoken = resetTokenService.findByToken(token.trim());

		if (rtoken == null)
			return ResponseUtil.errorResponse(MessageUtility.getErrorMessage("TokenError"), HttpStatus.UNAUTHORIZED);
		if (rtoken.isDeleted())
			return ResponseUtil.errorResponse(MessageUtility.getErrorMessage("TokenExpire"), HttpStatus.UNAUTHORIZED);

		if (KYCDateUtil.isLinkExpire(rtoken.getCreationDate(),
				Integer.parseInt(configuration.getResetExpirationTime()))) {
			rtoken.setDeleted(true);
			resetTokenService.save(rtoken);
			return ResponseUtil.errorResponse(MessageUtility.getErrorMessage("TokenExpire"), HttpStatus.UNAUTHORIZED);
		} else {
			User user = rtoken.getUser();
			user.setPassword(password.getPassword().trim());
			userservice.save(user);
			rtoken.setDeleted(true);
			resetTokenService.save(rtoken);
			return ResponseUtil.response(null, MessageUtility.getSuccessMessage("passwordUpdateNotification"));
		}
	}

	/**
	 * 
	 * @param token
	 * @param response
	 * @return
	 */
	@Activity(methodType = com.starin.constants.RequestMethod.PATCH, url = "api/v1/users/email/verify")
	@ApiOperation(value = "Activity For Verifying Email", response = ResponseEntity.class)
	@RequestMapping(value = "users/email/verify", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> verify(@RequestParam(required = false) String token, HttpServletResponse response) {

		if (token == null)
			return ResponseUtil.errorResponse(MessageUtility.getErrorMessage("EmptyToken"), HttpStatus.UNAUTHORIZED);
		int exp = Integer.parseInt(configuration.getVerificationExpirationTime().trim());
		VerificationToken vertoken = verificationservice.findByToken(token.trim());
		if (vertoken == null)
			return ResponseUtil.errorResponse(MessageUtility.getErrorMessage("VTokenError"), HttpStatus.UNAUTHORIZED);

		else if (KYCDateUtil.isLinkExpire(vertoken.getCreationDate(), exp) && !vertoken.isDeleted()) {
			verificationservice.onExpire(vertoken);
			return ResponseUtil.errorResponse(MessageUtility.getErrorMessage("Verification_TokenExpire"),
					HttpStatus.UNAUTHORIZED);
		} else {
			if (!vertoken.isDeleted()) {
				logger.debug("Verfication token is not deleted creating user from register user");

				Map result = userservice.createUser(vertoken.getRegisterUser());
				User user = (User) result.get("user");

				if (user == null) {
					return ResponseUtil.errorResponse(MessageUtility.getErrorMessage("databaseException"),
							HttpStatus.INTERNAL_SERVER_ERROR);
				}
				logger.debug("user created successfully from valid verification token");
				user.setIsActive(true);
				user.setIsEmailVerified(true);
				userservice.save(user);
				vertoken.setDeleted(true);
				vertoken.setUser(user);
				vertoken.setRegisterUser(null);
				verificationservice.save(vertoken);
				Context context = new Context();
				context.setVariable("username", user.getName());
				context.setVariable("secretKey", result.get("secretKey"));
				mailservice.sendScheduleHtmlMail(user, "Belrium Secret Key", context,
						KYCConstants.SecretKeyTemplateName);
				return ResponseUtil.response(null, MessageUtility.getSuccessMessage("emailVerified"));
			} else
				return ResponseUtil.response(null, MessageUtility.getSuccessMessage("alreadyEVerified"));
		}
	}

	// TODO
	/**
	 * @param request
	 * @return
	 */
	@Activity(methodType = com.starin.constants.RequestMethod.GET, url = "api/v1/user/profile")
	@ApiOperation(value = "Activity for Fetching User Profile Information", response = ResponseEntity.class)
	@RequestMapping(value = "/user/profile", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> userProfile(
			@RequestHeader(name = KYCConstants.KYC_Token_Header, defaultValue = "") String authToken,
			@RequestParam(required = false, defaultValue = "") String userId, HttpServletRequest request) {

		if (userId.equals(""))
			return ResponseUtil.errorResponse(MessageUtility.getErrorMessage("UserIdReq"), HttpStatus.BAD_REQUEST);
		Integer uid = null;
		try {
			uid = Integer.parseInt(userId);
		} catch (Exception e) {
			return ResponseUtil.errorResponse(MessageUtility.getErrorMessage("user_id_incorrect"),
					HttpStatus.BAD_REQUEST);
		}
		AuthenticationToken authenticationToken = authtokenservice.findByToken(authToken);
		User user = authenticationToken.getUser();
		if (user.getUid() != uid)
			return ResponseUtil.errorResponse(MessageUtility.getErrorMessage("NotLoggedInUser"),
					HttpStatus.UNAUTHORIZED);

		Country country = countryService.findByCountryID(Short.toUnsignedInt(user.getDefaultCountryId()));
		if (country == null) {
			return ResponseUtil.errorResponse(MessageUtility.getErrorMessage("CountryNotFound"),
					HttpStatus.UNAUTHORIZED);
		}
		HashMap<String, Object> profile = ObjectMap.objectMap(user);
		profile.remove("password");
		HashMap<String, Object> wallet_map = (HashMap<String, Object>) profile.get("wallet");
		if (wallet_map != null) {
			profile.put("wallet", wallet_map);
		}
		profile.remove("username");
		profile.remove("role");
		profile.remove("rolename");
		profile.put("country", ObjectMap.objectMap(country));

		return ResponseUtil.response(profile, MessageUtility.getSuccessMessage("profileMessage"));
	}

	// TODO
	/**
	 * @param request,user
	 * @return
	 */
	@Activity(methodType = com.starin.constants.RequestMethod.PATCH, url = "api/v1/user/profile")
	@ApiOperation(value = "Activity for update User Profile Data", response = ResponseEntity.class)
	@RequestMapping(value = "/user/profile", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> updateUserProfile(
			@RequestHeader(name = KYCConstants.KYC_Token_Header, defaultValue = "") String authToken,
			@RequestParam(required = false) String userId, @Valid @RequestBody Req_ProfileUpdate profile,
			BindingResult result) {

		if (userId.equals(""))
			return ResponseUtil.errorResponse(MessageUtility.getErrorMessage("UserIdReq"), HttpStatus.BAD_REQUEST);
		Integer uid = null;
		try {
			uid = Integer.parseInt(userId);
		} catch (Exception e) {
			return ResponseUtil.errorResponse(MessageUtility.getErrorMessage("user_id_incorrect"),
					HttpStatus.BAD_REQUEST);
		}
		if (result.hasErrors())
			return ResponseUtil.fieldErrorResponse(MessageUtility.getErrorMessage("FieldError"),
					KYCUtilities.getFieldErrorResponse(result));

		AuthenticationToken authenticationToken = authtokenservice.findByToken(authToken);
		User user = authenticationToken.getUser();
		if (user.getUid() != uid)
			return ResponseUtil.errorResponse(MessageUtility.getErrorMessage("NotLoggedInUser"),
					HttpStatus.UNAUTHORIZED);

		Map<String, Object> response = userservice.updateProfile(profile, authToken);
		return ResponseUtil.response(response.get("user"), MessageUtility.getSuccessMessage("profileUpdate"));

	}

	@Activity(methodType = com.starin.constants.RequestMethod.PATCH, url = "api/v1/users/password")
	@ApiOperation(value = "Activity for Changing user password after login", response = ResponseEntity.class)
	@PatchMapping(value = "/users/password", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> changePassword(
			@RequestHeader(value = KYCConstants.KYC_Token_Header) String starin_token,
			@Valid @RequestBody ChangePasswordCredential credential, BindingResult result) {
		if (starin_token == null)
			return ResponseUtil.response(null, MessageUtility.getErrorMessage("Token_Empty"));

		if (result.hasErrors()) {
			return ResponseUtil.fieldErrorResponse(MessageUtility.getErrorMessage("FieldError"),
					KYCUtilities.getFieldErrorResponse(result));
		}
		try {
			User user = authtokenservice.findByToken(starin_token.trim()).getUser();
			userservice.changePassword(user, credential);
			return ResponseUtil.response(null, MessageUtility.getSuccessMessage("changePasswordNotification"));
		} catch (PasswordMismatchException exception) {
			return ResponseUtil.errorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return ResponseUtil.errorResponse(MessageUtility.getErrorMessage("IntervalServerError"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}