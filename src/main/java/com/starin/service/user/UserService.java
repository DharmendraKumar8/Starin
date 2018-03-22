package com.starin.service.user;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.starin.domain.AuthenticationToken;
import com.starin.domain.VerificationToken;
import com.starin.domain.documents.Country;
import com.starin.domain.role.Role;
import com.starin.domain.user.Address;
import com.starin.domain.user.ChangePasswordCredential;
import com.starin.domain.user.HUserDetail;
import com.starin.domain.user.LoginHistory;
import com.starin.domain.user.RegisterUser;
import com.starin.domain.user.RegistrationCredential;
import com.starin.domain.user.Req_ProfileUpdate;
import com.starin.domain.user.User;
import com.starin.domain.user.UserCountry;
import com.starin.exceptions.CountryNotFoundException;
import com.starin.exceptions.PasswordMismatchException;
import com.starin.exceptions.RoleNotFoundException;
import com.starin.exceptions.UserAlreadyExistException;
import com.starin.exceptions.UserCountryMappingException;
import com.starin.exceptions.VerificationTokenNotVerifiedException;
import com.starin.hyperledger.service.APIRequestDataService;
import com.starin.hyperledger.service.HyperledgerApi;
import com.starin.repository.user.RegisterUserRepository;
import com.starin.repository.user.UserRepository;
import com.starin.service.CountryService;
import com.starin.service.HUserDetailService;
import com.starin.service.RoleService;
import com.starin.service.token.AuthTokenService;
import com.starin.service.token.TokenService;
import com.starin.service.token.VerificationTokenService;
import com.starin.utils.KYCUtilities;
import com.starin.utils.ObjectMap;

@Service
public class UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private CountryService countryService;
	@Autowired
	private RoleService roleservice;
	@Autowired
	private UserRepository userrepo;
	@Autowired
	private RegisterUserRepository registerUserRepository;
	@Autowired
	private TokenService tokenservice;
	@Autowired
	private VerificationTokenService vtokenservice;
	@Autowired
	private AuthTokenService authservice;
	@Autowired
	private LoginHistoryService loginHistoryService;
	@Autowired
	private UserCountryService userCountryService;
	@Autowired
	private HUserDetailService hUserDetailService;
	@Autowired
	private APIRequestDataService apiRequestDataService;
	@Autowired
	private HyperledgerApi hyperledgerApi;

	@Transactional
	public Map<String, Object> registerUser(RegistrationCredential credential)
			throws RoleNotFoundException, UserAlreadyExistException {

		Role role = null;
		Country country = countryService.findByCountryID(credential.parseCountryIdIntoInteger());
		if (country == null) {
			logger.debug("country not exist on countryId " + credential.getCountryId());
			throw new CountryNotFoundException("Country not found");
		}
		role = roleservice.findByRoleName(credential.getType());
		if (role == null) {
			logger.debug("role is null");
			throw new RoleNotFoundException("Not Valid role");
		}

		User user = findByEmail(credential.getEmail().trim());

		if (user != null) {
			logger.debug("user already exist");
			throw new UserAlreadyExistException("User Already Exist");
		}

		RegisterUser register_user = registerUserRepository.findByEmail(credential.getEmail());
		VerificationToken token = null;
		if (register_user == null) {
			register_user = new RegisterUser(credential.getName(),
					KYCUtilities.bcryptEncryptor(credential.getPassword().trim()), credential.getEmail().trim(), role,
					new Date(), country.getCountryID());
			logger.debug("data inside registeration credential");
			logger.debug("input name :" + credential.getName());
			logger.debug("input password :" + credential.getPassword().trim());
			logger.debug(credential.getEmail().trim());
			logger.debug("rolename : " + role.getRoleName());

			registerUserRepository.save(register_user);
			logger.debug("register user saved");
			token = new VerificationToken(tokenservice.genrate().trim(), new Date());
			token.setRegisterUser(register_user);
			vtokenservice.save(token);
			logger.debug("verification token generated for register user");
		} else {
			logger.debug("register user already exists");
			token = vtokenservice.findByRegisterUser(register_user);
			register_user.setRole(role);
			register_user.setName(credential.getName());
			register_user.setPassword(credential.getPassword());
			register_user.setCountryID(credential.parseCountryIdIntoInteger());
			Date newDate = new Date();
			try {
				logger.debug("creation Date of verification token" + token.getCreationDate());
				token.setCreationDate(newDate);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			token.setToken(tokenservice.genrate().trim());
			logger.debug("creation date and token updated");
		}
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("name", register_user.getName());
		res.put("email", register_user.getEmail());
		res.put("token", token.getToken());
		return res;
	}

	public Map<String, Object> getLoginToken(User user, HttpServletRequest request) {
		AuthenticationToken token = authservice.findByUserUid(user.getUid());
		Map<String, Object> data = ObjectMap.objectMap(user, "email~username~uid");
		if (token != null)
			token.setCreationDate(new Date());
		else {
			token = new AuthenticationToken();
			token.setUser(user);
		}
		token.setToken(tokenservice.genrate().trim());
		authservice.save(token);
		data.put("token", token.getToken().trim());
		data.put("lastLogin", loginHistoryService.getlastLogin(user.getUid()));
		data.put("roleId", user.getRole().getRoleId());
		LoginHistory loginHistory = new LoginHistory(new Date(), request.getRemoteAddr(),
				request.getHeader("User-Agent"));
		loginHistory.setUser(user);
		loginHistoryService.saveLoginHistory(loginHistory);
		return data;
	}

	@Transactional
	public User findByEmail(String email) {
		return userrepo.findByEmail(email);
	}

	public User save(User user) {
		return userrepo.save(user);
	}

	@Transactional
	public Map<String, Object> createUser(RegisterUser registerUser)
			throws RoleNotFoundException, UserCountryMappingException {

		Role role = null;
		role = registerUser.getRole();
		logger.debug("role fetched ");

		if (role == null) {
			throw new RoleNotFoundException("Not Valid role");
		}
		logger.debug("creation date for register_user" + registerUser.getCreatedAt());
		Country country = countryService.findByCountryID(registerUser.getCountryID());
		if (country == null) {
			logger.debug("country not exist on countryId " + registerUser.getCountryID());
			throw new CountryNotFoundException("Country not found");
		}
		Address address = new Address(country.getCountryName());
		Short defaultCountryId = Short.parseShort(country.getCountryID() + "");
		User user = new User(registerUser.getName(), registerUser.getPassword().trim(), registerUser.getEmail().trim(),
				role, registerUser.getCreatedAt(), address, defaultCountryId);
		logger.debug("user object created trying to store it in database");
		try {
			user = userrepo.save(user);
		} catch (Exception e) {
			logger.error("Exception in user creation", e);
			return null;
		}

		UserCountry userCountry = new UserCountry(user, country);
		try {
			userCountryService.save(userCountry);
		} catch (Exception e) {
			logger.error("Exception at saving UserCountry Mapping", e);
			throw new UserCountryMappingException("UserCountry not save in Database");
		}

		try {
			hUserDetailService.saveHUserDetail(new HUserDetail(user.getUid()));

		} catch (Exception e) {
			logger.error("Exception occur while save HUserDetail :  ", e);
		}
		JSONObject jsonRequest = apiRequestDataService.createJSONRequestForUserCreation(user.getUuid());
		if (jsonRequest == null) {
			throw new VerificationTokenNotVerifiedException(
					"Json request not created for register user on hyperledger");
		}

		JSONObject jsonResponse = hyperledgerApi.createUser(jsonRequest);
		if (jsonResponse == null) {
			throw new VerificationTokenNotVerifiedException("User not created on hyperledger");
		}
		String secretKey = null;
		try {
			user.sethPublicKey(jsonResponse.getString("publicKey"));
			secretKey = jsonResponse.getString("secret");
		} catch (Exception e) {
			throw new VerificationTokenNotVerifiedException("secret key not exist in create user api response");
		}
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("user", user);
		response.put("secretKey", secretKey);
		return response;
	}

	@Transactional
	public Map<String, Object> updateProfile(Req_ProfileUpdate profile, String authToken) {

		HashMap<String, Object> map = new HashMap<String, Object>();

		User user = authservice.findByToken(authToken.trim()).getUser();
		Field fieldname[] = profile.getClass().getDeclaredFields();
		for (Field field : fieldname) {
			String field_name = field.getName().trim();
			if (field_name.equals("address"))
				continue;
			try {
				Object updatedProperty = PropertyUtils.getProperty(profile, field_name);
				if (updatedProperty != null)
					PropertyUtils.setProperty(user, field_name, updatedProperty);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("code", -2);
				return map;
			}
		}
		if (profile.getAddress() != null) {
			Address address = user.getAddress();
			address.setAddressLine1(profile.getAddress().getAddressLine1());
			address.setAddressLine2(profile.getAddress().getAddressLine2());
			address.setCity(profile.getAddress().getCity());
			address.setPhone(profile.getAddress().getPhone());
			address.setPincode(profile.getAddress().getPincode());
			address.setState(profile.getAddress().getState());
		}
		map.put("code", 0);
		HashMap<String, Object> updated_user = ObjectMap.objectMap(user);
		updated_user.remove("password");
		List<Map<String, Object>> wallets_map = (List<Map<String, Object>>) updated_user.get("wallets");
		if (wallets_map != null) {
			updated_user.put("wallets", wallets_map);
		}
		updated_user.remove("username");
		updated_user.remove("role");
		updated_user.remove("rolename");
		map.put("user", updated_user);
		return map;
	}

	@Transactional
	public void changePassword(User user, ChangePasswordCredential credential) throws PasswordMismatchException {
		if (!KYCUtilities.bcryptEncryptorMatch(credential.getOldPassword(), user.getPassword())) {
			throw new PasswordMismatchException("OldPassword is not correct");
		} else if (!credential.getNewPassword().equals(credential.getConfirmPassword())) {
			throw new PasswordMismatchException("New password and confirm password" + "are not equal");
		} else {
			user.setPassword(credential.getNewPassword());
			userrepo.save(user);
		}

	}

}
