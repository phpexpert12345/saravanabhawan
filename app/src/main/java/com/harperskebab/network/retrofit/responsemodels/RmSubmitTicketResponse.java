package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.SerializedName;

public class RmSubmitTicketResponse {

    @SerializedName("error")
    private Long error;
    @SerializedName("error_msg")
    private String error_msg;

    public Long getError() {
        return error;
    }

    public void setError(Long error) {
        this.error = error;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
}
