package com.starin.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.starin.utils.KYCUtilities;


public class DevelopmentEnv implements EnvConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(DevelopmentEnv.class);
	
	@Value("${starin.db.name.dev}")
	private String dbName;
	@Value("${starin.db.driver}")
	private String dbDriverName;
	@Value("${starin.db.port.dev}")
	private String dbPort;
	@Value("${starin.db.ip.dev}")
	private String dbIpName;
	@Value("${starin.db.auth.dev.user}")
	private String dbUserName;
	@Value("${starin.db.auth.dev.pass}")
	private String dbPassword;
	@Value("${starin.frontendurl.dev}")
	private String frontEndUrl;
	@Value("${starin.db.name.mailfrom}")
	private String sender;
	@Value("${starin.frontendurl.dev.forgot}")
	private String forgotLink;
	@Value("${starin.default.roles}")
	private String[] roles ;
	@Value("${starin.package.controller}")
	private String[] controllerPackages ;

	@Value("${starin.default.adminEmailId}")
	private String adminEmailId;
	@Value("${starin.server.timezone}")
	private String serverTimezone;

	@Value("${starin.default.adminPassword}")
	private String adminPassword;

	@Value("${starin.default.adminRoleName}")
	private String adminRoleName;

	@Value("${starin.default.walletUpdateBatchSize}")
	private int walletUpdateBatchSize;
	@Value("${reset.expiration.time}")
	private String restLinkExpTime;

	@Value("${starin.default.adminCountry}")
	private String adminCountry;

	@Value("${verification.expiration.time}")
	private String verificationLinkExpTime;

	@Value("${starin.document.server.url}")
	private String kycDocumentServerUrl;

	@Value("${starin.document.root.path}")
	private String kycDocumentRootPath;

	@Value("${starin.document.nginx.directory}")
	private String nginxDirectorty;

	@Value("${starin.document.acceptedType}")
	private String[] acceptedType;
	
	
	@Value("${starin.default.superUserEmailId}")
	private String superUserEmail;
	
	@Value("${starin.default.superUserPassword}")
	private String superUserPassword;
	
	@Value("${starin.default.superUserCountry}")
	private String superUserCountry;
	
	@Value("${starin.default.userRolename}")
	private String userRolename;
	
	@Value("${starin.default.merchantRolename}")
	private String merchantRolename;
	
/*	@Value("${starin.document.forfield.type}")*/
	private String fieldTypes;
	
	@Value("${starin.miner.ip}")
	private String minerIp;
	
	@Value("${starin.miner.port}")
	private String adminMinerPort;
	
	@Value("${starin.miner.generate.address.url}")
	private String walletAddressUrl;
	
	@Value("${starin.miner.generate.address.batchsize}")
	private String walletAddressUserBatchSize;
	
	@Value("${starin.miner.candidateListUrl}")
	private String minerCandidateListUrl;

	@Value("${starin.db.connection.properties.maxActiveConnection.dev}")
	private String maxActiveDatabaseConnection;
	
	@Value("${starin.db.connection.properties.minidelConnection}")
	private String minIdelConnection;
	
	@Value("${starin.db.connection.properties.setInitialConnection.dev}")
	private String initialConnection;
	
	@Value("${starin.default.admin.allActivitiesToAdmin}")
	private String allActivityToAdminFlag;

	@Value("${authentication.expiration.time}")
	private String sessionExpirationTime;
	
	@Value("${starin.valid.registerationRole}")
	private String validRoleName;
	
	@Value("${starin.yearly.status.limit}")	
	private Integer yearlyKycStatusGraphLimit;
	
	@Value("${hyperledger.host.ip}")
	private String hyperLedgerIp;
	
	@Value("${hyperledger.host.port}")
	private String hyperLedgerPort;
	
	@Value("${hyperledger.api.document.upload}")
	private String hDocumentUploadApi;
	
	@Value("${hyperledger.api.register}")
	private String hUserRegisterApi;
	
	@Value("${hyperledger.api.document.login}")
	private String hUserTokenApi;
	
	@Value("${hyperledger.secret.max.restoreCount}")
	private String maxAllowedSecretKeyRequest;
	
	
	public String getValidRoleName(){
		return validRoleName;
	}
	public String getAdminCountry() {
		return (adminCountry != null) ? adminCountry.trim() : null;
	}

	@Override
	public String getDBName() {
		return dbName;
	}

	@Override
	public String getDBDriver() {
		return dbDriverName;
	}

	@Override
	public String getDBPort() {
		return dbPort;
	}

	@Override
	public String getDBIp() {
		return dbIpName;
	}

	public String getSender() {
		return sender;
	}

	@Override
	public String getDBUser() {
		return dbUserName;
	}

	@Override
	public String getDBPass() {
		return dbPassword;
	}

	@Override
	public String getFrontEndUrl() {

		return frontEndUrl;
	}

	@Override
	public String getForgotLink() {
		return forgotLink;
	}

	@Override
	public String[] getRoles() {
		return this.roles;
	}

	@Override
	public String[] getControllerPackages() {
		return this.controllerPackages;
	}

	@Override
	public String getAdminEmailId() {
		return (this.adminEmailId != null)?  this.adminEmailId.trim() : null;
	}

	@Override
	public String getAdminPassword() {
		return (this.adminPassword != null) ? this.adminPassword.trim() : null;
	}

	@Override
	public String getAdminRoleName() {
		return  (this.adminRoleName != null)? this.adminRoleName.trim() : null;
	}

	@Override
	public int getWalletUpdateBatchSize() {
		return walletUpdateBatchSize;
	}

	@Override
	public String getServerTimeZone() {
		// Server time zone
		return serverTimezone;
	}

	@Override
	public String getResetExpirationTime() {
		// Time for reset link expiration
		return restLinkExpTime;
	}

	@Override
	public String getVerificationExpirationTime() {
		// Time for verification link expiration.
		return verificationLinkExpTime;
	}

	@Override
	public String getKYCDocServerUrl() {
		return this.kycDocumentServerUrl;
	}

	@Override
	public String getKYCDocRootPath() {
		return this.kycDocumentRootPath;
	}

	@Override
	public String getNginxDirectory() {
		return this.nginxDirectorty;
	}

	@Override
	public String[] getDocumentAcceptedType() {
		return this.acceptedType;
	}

	@Override
	public String getSuperUserCountry() {
		return (superUserCountry != null) ? superUserCountry.trim() : null;
	}

	@Override
	public String getSuperUserPassword() {
		return (superUserPassword != null) ? superUserPassword.trim() : null;
	}

	@Override
	public String getSuperUserEmail() {
		return (superUserEmail != null) ? superUserEmail.trim() : null;
	}
	
	@Override
	public String getUserRolename() {
		return this.userRolename;
	}
	
	@Override
	public String getMerchantRolename() {
		return this.merchantRolename;
	}

	@Override
	public String getFieldType() {
		//todo, pattern check on filed type property 
		String typecandidate=this.fieldTypes;
		if(typecandidate==null){
			logger.debug("Please specify configuration property with key /'kyc.document.forfield.type candidate/', it can not be null");
//			System.exit(-1);
		}
		return typecandidate;
	}

	@Override
	public String getAdminMinerIp() {
		return this.minerIp;
	}

	@Override
	public String getAdminMinerPort() {
		return this.adminMinerPort;
	}

	@Override
	public String getWaleltAddressGenrateUrl() {
		return this.walletAddressUrl;
	}

	@Override
	public Integer getWalletAddressAssignBatchSize() {
		try{
			Integer.parseInt(this.walletAddressUserBatchSize);
		}catch(Exception e){
			logger.debug("batch size not integer",e);
		}
		return this.walletUpdateBatchSize;
	}

	@Override
	public String getMinerCandidateListUrl() {
		return minerCandidateListUrl;
	}

	@Override
	public Integer getMaxActiveDatabaseConnection() {
		Integer maxActive=KYCUtilities.parseIntoInteger(this.maxActiveDatabaseConnection);
		if(maxActive==null){
			logger.error("*********************Critical***************************");
			logger.error("Corrupted Max Integer connection value for max active database connection,sutting down server");
			//System.exit(-1);
		}
		return maxActive;
	}

	@Override
	public Integer getInitialDataBaseConnection() {
		Integer initialConnection=KYCUtilities.parseIntoInteger(this.initialConnection);
		if(initialConnection==null){
			logger.error("*********************Critical***************************");
			logger.error("Corrupted initial Connection Integer connection value for intial active database connection,sutting down server");
			//System.exit(-1);
		}
		return initialConnection;
	}

	@Override
	public Integer getMinimumIdelDataBaseConnection() {
		Integer minIdelDBConnection=KYCUtilities.parseIntoInteger(this.minIdelConnection);
		if(minIdelDBConnection==null){
			logger.error("*********************Critical***************************");
			logger.error("Corrupted minimal idle Connection Integer value connection value for intial active database connection,sutting down server");
			//System.exit(-1);
		}
		return minIdelDBConnection;
	}

	@Override
	public boolean getAssignAllActivityToAdminFlag() {
		try {
			Boolean flag=KYCUtilities.parseIntoBooleanValue(this.allActivityToAdminFlag);
			return flag;
		} catch (Exception e) {
			logger.debug("Not a boolean value , by default settng allActivityToAdminFlag to true",e);
			return true;
		}
	}

	@Override
	public String getSessionExpirationTime() {
		return this.sessionExpirationTime;
	}
	

	public Integer getYearlyKycStatusGraphLimit() {		
			 return (yearlyKycStatusGraphLimit == null ? 10 : this.yearlyKycStatusGraphLimit);
	}

	@Override
	public String getHyperLedgerIp() {
		return this.hyperLedgerIp;
	}
	@Override
	public String getHyperLedgerPort() {
		return this.hyperLedgerPort;
	}
	@Override
	public String getHDocumentUploadApi() {
		return this.hDocumentUploadApi;
	}
	@Override
	public String getHUserRegisterApi() {
		return this.hUserRegisterApi;
	}
	@Override
	public String getHUserTokenApi() {
		return this.hUserTokenApi;
	}
	@Override
	public Integer getMaxSecretRequestCount() {
		return Integer.parseInt(this.maxAllowedSecretKeyRequest);
	}
}
