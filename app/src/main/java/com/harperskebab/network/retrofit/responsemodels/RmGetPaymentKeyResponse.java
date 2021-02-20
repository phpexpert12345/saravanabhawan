
package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.SerializedName;

public class RmGetPaymentKeyResponse {

    @SerializedName("paypal_client_ID")
    private String paypalClientID;
    @SerializedName("paypal_secret")
    private String paypalSecret;
    @SerializedName("stripe_APIKey")
    private String stripeAPIKey;
    @SerializedName("stripe_publishKey")
    private String stripePublishKey;

    public String getPaypalClientID() {
        return paypalClientID;
    }

    public void setPaypalClientID(String paypalClientID) {
        this.paypalClientID = paypalClientID;
    }

    public String getPaypalSecret() {
        return paypalSecret;
    }

    public void setPaypalSecret(String paypalSecret) {
        this.paypalSecret = paypalSecret;
    }

    public String getStripeAPIKey() {
        return stripeAPIKey;
    }

    public void setStripeAPIKey(String stripeAPIKey) {
        this.stripeAPIKey = stripeAPIKey;
    }

    public String getStripePublishKey() {
        return stripePublishKey;
    }

    public void setStripePublishKey(String stripePublishKey) {
        this.stripePublishKey = stripePublishKey;
    }

}
