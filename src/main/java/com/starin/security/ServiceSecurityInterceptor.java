package com.starin.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.starin.constants.KYCConstants;
import com.starin.domain.role.Activity;
import com.starin.domain.role.Role;
import com.starin.service.ActivityService;
import com.starin.service.RoleActivityService;
import com.starin.service.RoleService;
import com.starin.service.token.AuthTokenService;
import com.starin.utils.KYCRoleUtil;
import com.starin.utils.KYCUtilities;
import com.starin.utils.MessageUtility;
import com.starin.utils.ResponseUtil;

/**
 * ServiceSecurityInterceptor for Role Based Security
 */

public class ServiceSecurityInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(ServiceSecurityInterceptor.class);

	@Autowired
	private ActivityService activityservice;

	@Autowired
	private RoleService roleservice;

	@Autowired
	private AuthTokenService authtokenservice;

	@Autowired
	private RoleActivityService roleActivityService;

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
	}

	/*
	 * Intercepter For enabling Role Based Security
	 * 
	 * @see
	 * org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.
	 * http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;

			String actionName = handlerMethod.getMethod().getName();
			logger.debug("Handler method name is :" + actionName);
			if (KYCUtilities.isPublicActivity(actionName.trim())) {
				logger.debug("Returning true");
				logger.debug("outside prehandle method of ServiceSecurityInterceptor");
				return true;
			} else {
				String HandlerClassQualifiedName = handlerMethod.getBean().getClass().getName();
				Activity activity = activityservice.findByHandlerMethodName(
						HandlerClassQualifiedName + KYCConstants.ActivitySeprator + actionName);
				if (activity == null) {
					logger.debug("Activity does not exists");
					ResponseUtil.genrate(response, MessageUtility.getErrorMessage("UnknownActivity"),
							HttpServletResponse.SC_UNAUTHORIZED);
					return false;
				}
				Role role = authtokenservice.findByToken(request.getHeader(KYCConstants.KYC_Token_Header).trim())
						.getUser().getRole();
				logger.debug("Activity id : " + activity.getActivityId());
				logger.debug("Role id     : " + role.getRoleId());
				logger.debug("Activity being called is : " + actionName);
				logger.debug("HandlerClassQualifiedName : " + HandlerClassQualifiedName);
				logger.debug(
						"searched name :" + HandlerClassQualifiedName + KYCConstants.ActivitySeprator + actionName);
				if (KYCRoleUtil.level(role.getRoleName()) == 1)
					return true;

				Activity currentRoleActivty = roleActivityService.getActivityByRole(role.getRoleId(),
						activity.getActivityId());
				// List<Object[]>
				// role_Activites=roleservice.getRoleActivites(role.getRoleId(),activity.getActivityId());

				if (currentRoleActivty == null) {
					ResponseUtil.genrate(response, MessageUtility.getErrorMessage("AccessDenid"),
							HttpServletResponse.SC_UNAUTHORIZED);
					logger.debug("outside prehandle method of ServiceSecurityInterceptor");
					return false;
				} else {
					if (!role.isActive() || !activity.isActive()) {
						ResponseUtil.genrate(response, MessageUtility.getErrorMessage("AccessDenid"),
								HttpServletResponse.SC_UNAUTHORIZED);
						return false;
					}
					logger.debug("selected role id :" + role.getRoleId());
					logger.debug("selected activity id :" + currentRoleActivty.getActivityId());
					logger.debug("outside prehandle method of ServiceSecurityInterceptor");
					return true;
				}
			}
		} else {
			return true;
		}

	}
}