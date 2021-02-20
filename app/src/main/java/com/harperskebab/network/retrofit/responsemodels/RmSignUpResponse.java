package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.SerializedName;

public class RmSignUpResponse {

    @SerializedName("CustomerId")
    private String customerId;
    @SerializedName("digital_wallet_card_number")
    private String digitalWalletCardNumber;
    @SerializedName("digital_wallet_card_pin_number")
    private String digitalWalletCardPinNumber;
    @SerializedName("digital_wallet_member_since")
    private String digitalWalletMemberSince;
    @SerializedName("digital_wallet_qr_code")
    private String digitalWalletQrCode;
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
    @SerializedName("Total_wallet_money_available")
    private String totalWalletMoneyAvailable;
    @SerializedName("WEBSITE_DIGITAL_WALLET_TERMS")
    private Object wEBSITEDIGITALWALLETTERMS;
    @SerializedName("error")
    private Long error;
    @SerializedName("error_msg")
    private String errorMsg;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public Object getWEBSITEDIGITALWALLETTERMS() {
        return wEBSITEDIGITALWALLETTERMS;
    }

    public void setWEBSITEDIGITALWALLETTERMS(Object wEBSITEDIGITALWALLETTERMS) {
        this.wEBSITEDIGITALWALLETTERMS = wEBSITEDIGITALWALLETTERMS;
    }

    public Long getError() {
        return error;
    }

    public void setError(Long error) {
        this.error = error;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
