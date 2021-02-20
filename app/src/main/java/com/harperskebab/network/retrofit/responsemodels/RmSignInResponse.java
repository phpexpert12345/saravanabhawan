package com.harperskebab.network.retrofit.responsemodels;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class RmSignInResponse {

    @SerializedName("Account_serial_No")
    private String accountSerialNo;
    @SerializedName("company_street")
    private String companyStreet;
    @SerializedName("customerCity")
    private String customerCity;
    @SerializedName("customerCountry")
    private String customerCountry;
    @SerializedName("customerFlat_Name")
    private String customerFlatName;
    @SerializedName("customerFloor_House_Number")
    private String customerFloorHouseNumber;
    @SerializedName("CustomerId")
    private String customerId;
    @SerializedName("customerState")
    private String customerState;
    @SerializedName("customeraddresslat")
    private String customeraddresslat;
    @SerializedName("customeraddresslong")
    private String customeraddresslong;
    @SerializedName("digital_wallet_card_number")
    private String digitalWalletCardNumber;
    @SerializedName("digital_wallet_card_pin_number")
    private String digitalWalletCardPinNumber;
    @SerializedName("digital_wallet_member_since")
    private String digitalWalletMemberSince;
    @SerializedName("digital_wallet_qr_code")
    private String digitalWalletQrCode;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("referral_code")
    private String referralCode;
    @SerializedName("referral_codeMessage")
    private String referralCodeMessage;
    @SerializedName("referral_earn_points")
    private String referralEarnPoints;
    @SerializedName("referral_join_friends")
    private String referralJoinFriends;
    @SerializedName("referral_sharing_Message")
    private String referralSharingMessage;
    @SerializedName("success")
    private Long success;
    @SerializedName("success_msg")
    private String successMsg;
    @SerializedName("error_msg")
    private String errorMsg;
    @SerializedName("Total_wallet_money_available")
    private String totalWalletMoneyAvailable;
    @SerializedName("user_address")
    private String userAddress;
    @SerializedName("user_city")
    private String userCity;
    @SerializedName("user_email")
    private String userEmail;
    @SerializedName("user_name")
    private String userName;
    @SerializedName("user_phone")
    private String userPhone;
    @SerializedName("user_photo")
    private String userPhoto;
    @SerializedName("user_postcode")
    private String userPostcode;
    @SerializedName("user_state")
    private String userState;
    @SerializedName("WEBSITE_DIGITAL_WALLET_TERMS")
    private Object wEBSITEDIGITALWALLETTERMS;
    @SerializedName("branch_id")
    private String branch_id;

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getAccountSerialNo() {
        return accountSerialNo;
    }

    public void setAccountSerialNo(String accountSerialNo) {
        this.accountSerialNo = accountSerialNo;
    }

    public String getCompanyStreet() {
        return companyStreet;
    }

    public void setCompanyStreet(String companyStreet) {
        this.companyStreet = companyStreet;
    }

    public String getCustomerCity() {
        return customerCity;
    }

    public void setCustomerCity(String customerCity) {
        this.customerCity = customerCity;
    }

    public String getCustomerCountry() {
        return customerCountry;
    }

    public void setCustomerCountry(String customerCountry) {
        this.customerCountry = customerCountry;
    }

    public String getCustomerFlatName() {
        return customerFlatName;
    }

    public void setCustomerFlatName(String customerFlatName) {
        this.customerFlatName = customerFlatName;
    }

    public String getCustomerFloorHouseNumber() {
        return customerFloorHouseNumber;
    }

    public void setCustomerFloorHouseNumber(String customerFloorHouseNumber) {
        this.customerFloorHouseNumber = customerFloorHouseNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerState() {
        return customerState;
    }

    public void setCustomerState(String customerState) {
        this.customerState = customerState;
    }

    public String getCustomeraddresslat() {
        return customeraddresslat;
    }

    public void setCustomeraddresslat(String customeraddresslat) {
        this.customeraddresslat = customeraddresslat;
    }

    public String getCustomeraddresslong() {
        return customeraddresslong;
    }

    public void setCustomeraddresslong(String customeraddresslong) {
        this.customeraddresslong = customeraddresslong;
    }

    public String getDigitalWalletCardNumber() {
        return digitalWalletCardNumber;
    }

    public void setDigitalWalletCardNumber(String digitalWalletCardNumber) {
        this.digitalWalletCardNumber = digitalWalletCardNumber;
    }

    public String getDigitalWalletCardPinNumber() {
        return digitalWalletCardPinNumber;
    }

    public void setDigitalWalletCardPinNumber(String digitalWalletCardPinNumber) {
        this.digitalWalletCardPinNumber = digitalWalletCardPinNumber;
    }

    public String getDigitalWalletMemberSince() {
        return digitalWalletMemberSince;
    }

    public void setDigitalWalletMemberSince(String digitalWalletMemberSince) {
        this.digitalWalletMemberSince = digitalWalletMemberSince;
    }

    public String getDigitalWalletQrCode() {
        return digitalWalletQrCode;
    }

    public void setDigitalWalletQrCode(String digitalWalletQrCode) {
        this.digitalWalletQrCode = digitalWalletQrCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getReferralCodeMessage() {
        return referralCodeMessage;
    }

    public void setReferralCodeMessage(String referralCodeMessage) {
        this.referralCodeMessage = referralCodeMessage;
    }

    public String getReferralEarnPoints() {
        return referralEarnPoints;
    }

    public void setReferralEarnPoints(String referralEarnPoints) {
        this.referralEarnPoints = referralEarnPoints;
    }

    public String getReferralJoinFriends() {
        return referralJoinFriends;
    }

    public void setReferralJoinFriends(String referralJoinFriends) {
        this.referralJoinFriends = referralJoinFriends;
    }

    public String getReferralSharingMessage() {
        return referralSharingMessage;
    }

    public void setReferralSharingMessage(String referralSharingMessage) {
        this.referralSharingMessage = referralSharingMessage;
    }

    public Long getSuccess() {
        return success;
    }

    public void setSuccess(Long success) {
        this.success = success;
    }

    public String getSuccessMsg() {
        return successMsg;
    }

    public void setSuccessMsg(String successMsg) {
        this.successMsg = successMsg;
    }

    public String getTotalWalletMoneyAvailable() {
        return totalWalletMoneyAvailable;
    }

    public void setTotalWalletMoneyAvailable(String totalWalletMoneyAvailable) {
        this.totalWalletMoneyAvailable = totalWalletMoneyAvailable;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserPostcode() {
        return userPostcode;
    }

    public void setUserPostcode(String userPostcode) {
        this.userPostcode = userPostcode;
    }

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public Object getWEBSITEDIGITALWALLETTERMS() {
        return wEBSITEDIGITALWALLETTERMS;
    }

    public void setWEBSITEDIGITALWALLETTERMS(Object wEBSITEDIGITALWALLETTERMS) {
        this.wEBSITEDIGITALWALLETTERMS = wEBSITEDIGITALWALLETTERMS;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
