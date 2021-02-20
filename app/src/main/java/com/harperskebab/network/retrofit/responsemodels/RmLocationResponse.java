
package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RmLocationResponse {

    @SerializedName("api_key")
    private String apiKey;
    @SerializedName("customer_city")
    private String customerCity;
    @SerializedName("customer_country")
    private String customerCountry;
    @SerializedName("customer_country_code")
    private String customerCountryCode;
    @SerializedName("customer_currency")
    private String customerCurrency;
    @SerializedName("customer_default_langauge")
    private String customerDefaultLangauge;
    @SerializedName("customer_full_address")
    private String customerFullAddress;
    @SerializedName("customer_lat")
    private String customerLat;
    @SerializedName("customer_locality")
    private String customerLocality;
    @SerializedName("customer_long")
    private String customerLong;
    @SerializedName("customer_postcode")
    private String customerPostcode;
    @SerializedName("customer_search")
    private String customerSearch;
    @SerializedName("customer_search_display")
    private String customerSearchDisplay;
    @SerializedName("customer_search_text")
    private String customerSearchText;
    @Expose
    private String success;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
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

    public String getCustomerCountryCode() {
        return customerCountryCode;
    }

    public void setCustomerCountryCode(String customerCountryCode) {
        this.customerCountryCode = customerCountryCode;
    }

    public String getCustomerCurrency() {
        return customerCurrency;
    }

    public void setCustomerCurrency(String customerCurrency) {
        this.customerCurrency = customerCurrency;
    }

    public String getCustomerDefaultLangauge() {
        return customerDefaultLangauge;
    }

    public void setCustomerDefaultLangauge(String customerDefaultLangauge) {
        this.customerDefaultLangauge = customerDefaultLangauge;
    }

    public String getCustomerFullAddress() {
        return customerFullAddress;
    }

    public void setCustomerFullAddress(String customerFullAddress) {
        this.customerFullAddress = customerFullAddress;
    }

    public String getCustomerLat() {
        return customerLat;
    }

    public void setCustomerLat(String customerLat) {
        this.customerLat = customerLat;
    }

    public String getCustomerLocality() {
        return customerLocality;
    }

    public void setCustomerLocality(String customerLocality) {
        this.customerLocality = customerLocality;
    }

    public String getCustomerLong() {
        return customerLong;
    }

    public void setCustomerLong(String customerLong) {
        this.customerLong = customerLong;
    }

    public String getCustomerPostcode() {
        return customerPostcode;
    }

    public void setCustomerPostcode(String customerPostcode) {
        this.customerPostcode = customerPostcode;
    }

    public String getCustomerSearch() {
        return customerSearch;
    }

    public void setCustomerSearch(String customerSearch) {
        this.customerSearch = customerSearch;
    }

    public String getCustomerSearchDisplay() {
        return customerSearchDisplay;
    }

    public void setCustomerSearchDisplay(String customerSearchDisplay) {
        this.customerSearchDisplay = customerSearchDisplay;
    }

    public String getCustomerSearchText() {
        return customerSearchText;
    }

    public void setCustomerSearchText(String customerSearchText) {
        this.customerSearchText = customerSearchText;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

}
