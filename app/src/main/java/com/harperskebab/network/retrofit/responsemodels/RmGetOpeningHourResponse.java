
package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.SerializedName;

public class RmGetOpeningHourResponse {

    @SerializedName("FridayText")
    private String fridayText;
    @SerializedName("MondayText")
    private String mondayText;
    @SerializedName("SaturdayText")
    private String saturdayText;
    @SerializedName("success")
    private String success;
    @SerializedName("success_msg")
    private String successMsg;
    @SerializedName("SundayText")
    private String sundayText;
    @SerializedName("ThursdayText")
    private String thursdayText;
    @SerializedName("TuesdayText")
    private String tuesdayText;
    @SerializedName("WednesdayText")
    private String wednesdayText;

    public String getFridayText() {
        return fridayText;
    }

    public void setFridayText(String fridayText) {
        this.fridayText = fridayText;
    }

    public String getMondayText() {
        return mondayText;
    }

    public void setMondayText(String mondayText) {
        this.mondayText = mondayText;
    }

    public String getSaturdayText() {
        return saturdayText;
    }

    public void setSaturdayText(String saturdayText) {
        this.saturdayText = saturdayText;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getSuccessMsg() {
        return successMsg;
    }

    public void setSuccessMsg(String successMsg) {
        this.successMsg = successMsg;
    }

    public String getSundayText() {
        return sundayText;
    }

    public void setSundayText(String sundayText) {
        this.sundayText = sundayText;
    }

    public String getThursdayText() {
        return thursdayText;
    }

    public void setThursdayText(String thursdayText) {
        this.thursdayText = thursdayText;
    }

    public String getTuesdayText() {
        return tuesdayText;
    }

    public void setTuesdayText(String tuesdayText) {
        this.tuesdayText = tuesdayText;
    }

    public String getWednesdayText() {
        return wednesdayText;
    }

    public void setWednesdayText(String wednesdayText) {
        this.wednesdayText = wednesdayText;
    }

}
