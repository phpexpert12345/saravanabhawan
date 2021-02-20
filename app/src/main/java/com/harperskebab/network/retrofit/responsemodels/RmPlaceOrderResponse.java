package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.SerializedName;

public class RmPlaceOrderResponse {

    @SerializedName("CouponCode")
    private String couponCode;
    @SerializedName("CouponCodePrice")
    private String couponCodePrice;
    @SerializedName("couponCodeType")
    private String couponCodeType;
    @SerializedName("CustomerId")
    private String customerId;
    @SerializedName("deliveryChargeValueType")
    private String deliveryChargeValueType;
    @SerializedName("deliveryChrg")
    private Object deliveryChrg;
    @SerializedName("deliverydate")
    private String deliverydate;
    @SerializedName("discountOfferDescription")
    private String discountOfferDescription;
    @SerializedName("discountOfferPrice")
    private String discountOfferPrice;
    @SerializedName("email")
    private String email;
    @SerializedName("ip")
    private String ip;
    @SerializedName("name_customer")
    private String nameCustomer;
    @SerializedName("odr_date")
    private String odrDate;
    @SerializedName("ordPrice")
    private String ordPrice;
    @SerializedName("order_identifyno")
    private String orderIdentifyno;
    @SerializedName("order_no")
    private String orderNo;
    @SerializedName("order_type")
    private String orderType;
    @SerializedName("PackageFees")
    private String packageFees;
    @SerializedName("PackageFeesType")
    private String packageFeesType;
    @SerializedName("payMode")
    private String payMode;
    @SerializedName("PaymentProcessingFees")
    private String paymentProcessingFees;
    @SerializedName("preorderTime")
    private String preorderTime;
    @SerializedName("requestTime")
    private String requestTime;
    @SerializedName("restaurant_name")
    private String restaurantName;
    @SerializedName("RestaurantoffrType")
    private String restaurantoffrType;
    @SerializedName("restoid")
    private Long restoid;
    @SerializedName("ServiceFees")
    private String serviceFees;
    @SerializedName("ServiceFeesType")
    private String serviceFeesType;
    @SerializedName("status")
    private String status;
    @SerializedName("success")
    private Long success;
    @SerializedName("type")
    private String type;
    @SerializedName("user_date")
    private String userDate;
    @SerializedName("VatTax")
    private String vatTax;
    @SerializedName("WebsiteCodeNo")
    private String websiteCodeNo;
    @SerializedName("WebsiteCodePrice")
    private String websiteCodePrice;
    @SerializedName("WebsiteCodeType")
    private String websiteCodeType;

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCouponCodePrice() {
        return couponCodePrice;
    }

    public void setCouponCodePrice(String couponCodePrice) {
        this.couponCodePrice = couponCodePrice;
    }

    public String getCouponCodeType() {
        return couponCodeType;
    }

    public void setCouponCodeType(String couponCodeType) {
        this.couponCodeType = couponCodeType;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDeliveryChargeValueType() {
        return deliveryChargeValueType;
    }

    public void setDeliveryChargeValueType(String deliveryChargeValueType) {
        this.deliveryChargeValueType = deliveryChargeValueType;
    }

    public Object getDeliveryChrg() {
        return deliveryChrg;
    }

    public void setDeliveryChrg(Object deliveryChrg) {
        this.deliveryChrg = deliveryChrg;
    }

    public String getDeliverydate() {
        return deliverydate;
    }

    public void setDeliverydate(String deliverydate) {
        this.deliverydate = deliverydate;
    }

    public String getDiscountOfferDescription() {
        return discountOfferDescription;
    }

    public void setDiscountOfferDescription(String discountOfferDescription) {
        this.discountOfferDescription = discountOfferDescription;
    }

    public String getDiscountOfferPrice() {
        return discountOfferPrice;
    }

    public void setDiscountOfferPrice(String discountOfferPrice) {
        this.discountOfferPrice = discountOfferPrice;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNameCustomer() {
        return nameCustomer;
    }

    public void setNameCustomer(String nameCustomer) {
        this.nameCustomer = nameCustomer;
    }

    public String getOdrDate() {
        return odrDate;
    }

    public void setOdrDate(String odrDate) {
        this.odrDate = odrDate;
    }

    public String getOrdPrice() {
        return ordPrice;
    }

    public void setOrdPrice(String ordPrice) {
        this.ordPrice = ordPrice;
    }

    public String getOrderIdentifyno() {
        return orderIdentifyno;
    }

    public void setOrderIdentifyno(String orderIdentifyno) {
        this.orderIdentifyno = orderIdentifyno;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getPackageFees() {
        return packageFees;
    }

    public void setPackageFees(String packageFees) {
        this.packageFees = packageFees;
    }

    public String getPackageFeesType() {
        return packageFeesType;
    }

    public void setPackageFeesType(String packageFeesType) {
        this.packageFeesType = packageFeesType;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getPaymentProcessingFees() {
        return paymentProcessingFees;
    }

    public void setPaymentProcessingFees(String paymentProcessingFees) {
        this.paymentProcessingFees = paymentProcessingFees;
    }

    public String getPreorderTime() {
        return preorderTime;
    }

    public void setPreorderTime(String preorderTime) {
        this.preorderTime = preorderTime;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantoffrType() {
        return restaurantoffrType;
    }

    public void setRestaurantoffrType(String restaurantoffrType) {
        this.restaurantoffrType = restaurantoffrType;
    }

    public Long getRestoid() {
        return restoid;
    }

    public void setRestoid(Long restoid) {
        this.restoid = restoid;
    }

    public String getServiceFees() {
        return serviceFees;
    }

    public void setServiceFees(String serviceFees) {
        this.serviceFees = serviceFees;
    }

    public String getServiceFeesType() {
        return serviceFeesType;
    }

    public void setServiceFeesType(String serviceFeesType) {
        this.serviceFeesType = serviceFeesType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getSuccess() {
        return success;
    }

    public void setSuccess(Long success) {
        this.success = success;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserDate() {
        return userDate;
    }

    public void setUserDate(String userDate) {
        this.userDate = userDate;
    }

    public String getVatTax() {
        return vatTax;
    }

    public void setVatTax(String vatTax) {
        this.vatTax = vatTax;
    }

    public String getWebsiteCodeNo() {
        return websiteCodeNo;
    }

    public void setWebsiteCodeNo(String websiteCodeNo) {
        this.websiteCodeNo = websiteCodeNo;
    }

    public String getWebsiteCodePrice() {
        return websiteCodePrice;
    }

    public void setWebsiteCodePrice(String websiteCodePrice) {
        this.websiteCodePrice = websiteCodePrice;
    }

    public String getWebsiteCodeType() {
        return websiteCodeType;
    }

    public void setWebsiteCodeType(String websiteCodeType) {
        this.websiteCodeType = websiteCodeType;
    }

}
