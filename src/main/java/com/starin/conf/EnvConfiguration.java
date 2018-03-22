package com.starin.conf;

public interface EnvConfiguration {
	String getDBName();

	String getDBDriver();

	String getDBPort();

	String getDBIp();

	String getDBUser();

	String getDBPass();

	String getFrontEndUrl();

	String getForgotLink();

	String getSender();

	String[] getRoles();

	String[] getControllerPackages();

	String getAdminEmailId();

	String getAdminPassword();

	String getAdminRoleName();

	int getWalletUpdateBatchSize();

	String getServerTimeZone();

	String getResetExpirationTime();

	String getVerificationExpirationTime();

	String getSuperUserCountry();

	String getSuperUserPassword();

	String getSuperUserEmail();

	String getAdminCountry();

	String getKYCDocServerUrl();

	String getKYCDocRootPath();

	String getNginxDirectory();

	String getUserRolename();

	String getMerchantRolename();

	String[] getDocumentAcceptedType();

	String getFieldType();

	String getAdminMinerIp();

	String getAdminMinerPort();

	String getWaleltAddressGenrateUrl();

	Integer getWalletAddressAssignBatchSize();

	String getMinerCandidateListUrl();

	Integer getMaxActiveDatabaseConnection();

	Integer getInitialDataBaseConnection();

	Integer getMinimumIdelDataBaseConnection();

	boolean getAssignAllActivityToAdminFlag();

	String getSessionExpirationTime();

	String getValidRoleName();

	Integer getYearlyKycStatusGraphLimit();

	String getHyperLedgerIp();

	String getHyperLedgerPort();

	String getHDocumentUploadApi();

	String getHUserRegisterApi();

	String getHUserTokenApi();

	Integer getMaxSecretRequestCount();

}
