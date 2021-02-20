
package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.SerializedName;

public class RmBookATableResponse {

    @SerializedName("booking_date")
    private String bookingDate;
    @SerializedName("booking_instruction")
    private String bookingInstruction;
    @SerializedName("booking_mobile")
    private String bookingMobile;
    @SerializedName("Booking_Number")
    private String bookingNumber;
    @SerializedName("booking_time")
    private String bookingTime;
    @SerializedName("Important_Note")
    private String importantNote;
    @SerializedName("Number_of_person")
    private String numberOfPerson;
    @SerializedName("success")
    private Long success;
    @SerializedName("success_msg")
    private String successMsg;
    @SerializedName("Thankyou")
    private String thankyou;
    @SerializedName("Total_bill_amount")
    private String totalBillAmount;
    @SerializedName("Total_bill_discount_amount")
    private String totalBillDiscountAmount;
    @SerializedName("Total_Deposit_Amount")
    private String totalDepositAmount;
    @SerializedName("Total_Final_Amount")
    private String totalFinalAmount;
    @SerializedName("Total_Service_Charge")
    private String totalServiceCharge;
    @SerializedName("Total_Sub_Total_after_discount")
    private String totalSubTotalAfterDiscount;
    @SerializedName("Transaction_Number")
    private String transactionNumber;

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingInstruction() {
        return bookingInstruction;
    }

    public void setBookingInstruction(String bookingInstruction) {
        this.bookingInstruction = bookingInstruction;
    }

    public String getBookingMobile() {
        return bookingMobile;
    }

    public void setBookingMobile(String bookingMobile) {
        this.bookingMobile = bookingMobile;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getImportantNote() {
        return importantNote;
    }

    public void setImportantNote(String importantNote) {
        this.importantNote = importantNote;
    }

    public String getNumberOfPerson() {
        return numberOfPerson;
    }

    public void setNumberOfPerson(String numberOfPerson) {
        this.numberOfPerson = numberOfPerson;
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

    public String getThankyou() {
        return thankyou;
    }

    public void setThankyou(String thankyou) {
        this.thankyou = thankyou;
    }

    public String getTotalBillAmount() {
        return totalBillAmount;
    }

    public void setTotalBillAmount(String totalBillAmount) {
        this.totalBillAmount = totalBillAmount;
    }

    public String getTotalBillDiscountAmount() {
        return totalBillDiscountAmount;
    }

    public void setTotalBillDiscountAmount(String totalBillDiscountAmount) {
        this.totalBillDiscountAmount = totalBillDiscountAmount;
    }

    public String getTotalDepositAmount() {
        return totalDepositAmount;
    }

    public void setTotalDepositAmount(String totalDepositAmount) {
        this.totalDepositAmount = totalDepositAmount;
    }

    public String getTotalFinalAmount() {
        return totalFinalAmount;
    }

    public void setTotalFinalAmount(String totalFinalAmount) {
        this.totalFinalAmount = totalFinalAmount;
    }

    public String getTotalServiceCharge() {
        return totalServiceCharge;
    }

    public void setTotalServiceCharge(String totalServiceCharge) {
        this.totalServiceCharge = totalServiceCharge;
    }

    public String getTotalSubTotalAfterDiscount() {
        return totalSubTotalAfterDiscount;
    }

    public void setTotalSubTotalAfterDiscount(String totalSubTotalAfterDiscount) {
        this.totalSubTotalAfterDiscount = totalSubTotalAfterDiscount;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

}
