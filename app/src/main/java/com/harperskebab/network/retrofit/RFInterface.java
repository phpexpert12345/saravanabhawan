package com.harperskebab.network.retrofit;

import com.harperskebab.model.Restaurant;
import com.harperskebab.network.retrofit.responsemodels.RmAddAddressResponse;
import com.harperskebab.network.retrofit.responsemodels.RmBookATableResponse;
import com.harperskebab.network.retrofit.responsemodels.RmChangePasswordResponse;
import com.harperskebab.network.retrofit.responsemodels.RmDeleteAddressResponse;
import com.harperskebab.network.retrofit.responsemodels.RmForgetPasswordResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetAddressResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetAllergyResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetBranchResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetCardsResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetComboExtraToppingListResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetComboListResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetDeliveryAreaResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetDeliveryTimeSlotResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetFoodCategoryResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetFoodItemExtraToppingResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetFoodItemResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetFoodResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetFrontBannerResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetGalleryResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetLoyaltyPointListResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetLoyaltyPointResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetOpeningHourResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetOrderDetailResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetOrderListResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetPaymentKeyResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetReOrderResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetRestaurantDiscountResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetRestaurantOfferResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetRestaurantReviewResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetReviewsResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetServiceChargeResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetSplashResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetTableListResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetTicketResponse;
import com.harperskebab.network.retrofit.responsemodels.RmLanguageListResponse;
import com.harperskebab.network.retrofit.responsemodels.RmLanguageResponse;
import com.harperskebab.network.retrofit.responsemodels.RmLocationResponse;
import com.harperskebab.network.retrofit.responsemodels.RmOrderCancelResponse;
import com.harperskebab.network.retrofit.responsemodels.RmOrderSendToKitchenResponse;
import com.harperskebab.network.retrofit.responsemodels.RmPayLaterPlaceOrderResponse;
import com.harperskebab.network.retrofit.responsemodels.RmPlaceOrderResponse;
import com.harperskebab.network.retrofit.responsemodels.RmSignInResponse;
import com.harperskebab.network.retrofit.responsemodels.RmSignUpResponse;
import com.harperskebab.network.retrofit.responsemodels.RmStripeUpdateChargeResponse;
import com.harperskebab.network.retrofit.responsemodels.RmSubmitContactUsResponse;
import com.harperskebab.network.retrofit.responsemodels.RmSubmitTicketResponse;
import com.harperskebab.network.retrofit.responsemodels.RmTrackOrderResponse;
import com.harperskebab.network.retrofit.responsemodels.RmUpdateProfileResponse;
import com.harperskebab.network.retrofit.responsemodels.RmUpdateReviewResponse;
import com.harperskebab.network.retrofit.responsemodels.RmVerifiyPostalcodeResponse;
import com.harperskebab.network.retrofit.responsemodels.RmVerifyCouponCodeResponse;
import com.harperskebab.network.retrofit.responsemodels.RmVerifyLoyaltyPointResponse;
import com.harperskebab.utils.Constant;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RFInterface {

    @GET(Constant.Url.LOCATION)
    Observable<RmLocationResponse> checkLocation(
            @Query("customer_lat") String latitude,
            @Query("customer_long") String longitude,
            @Query("api_key") String api_key
    );

    @GET(Constant.Url.LANGUAGE)
    Observable<RmLanguageResponse> getLanguage(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code
    );

    @GET(Constant.Url.LANGUAGE_LIST)
    Observable<RmLanguageListResponse> getLanguageList(
            @Query("api_key") String api_key
    );

    @GET(Constant.Url.SPLASH)
    Observable<RmGetSplashResponse> getSplash(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("splash_type") String splash_type
    );

    @GET(Constant.Url.RESTRURENT_INFORMATION)
    Observable<Restaurant> getRestrurentInformation(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code
    );

    @GET(Constant.Url.HOME_SLIDER)
    Observable<RmGetFrontBannerResponse> getFrontBanner(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("module_call") String module_call,
            @Query("branch_id") String branchId
    );

    @GET(Constant.Url.FOOD_CATEGORY)
    Observable<RmGetFoodCategoryResponse> getFoodCategory(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("branch_id") String branchId
    );

    @GET(Constant.Url.FOOD)
    Observable<RmGetFoodResponse> getFood(
            @Query("branch_id") String branch_id,
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("Category_ID") String Category_ID,
            @Query("resid") String resid
    );

    @GET(Constant.Url.FOOD_COMBO)
    Observable<RmGetComboListResponse> getComboFood(
            @Query("branch_id") String branch_id,
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("resid") String resid,
            @Query("RestaurantCategoryID") String Category_ID
    );
    @GET(Constant.Url.FOOD_COMBO_ITEM_EXTRA_TOPPING)
    Observable<RmGetComboExtraToppingListResponse> getComboExtraTopping(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("ItemID") String resid,
            @Query("comboslot_id") String comboslot_id,
            @Query("FoodItemSizeID") String foodItemSizeID
    );
    @GET(Constant.Url.FOOD_ITEM)
    Observable<RmGetFoodItemResponse> getFoodItem(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("ItemID") String food_ID
    );

    @GET(Constant.Url.FOOD_ITEM_EXTRA_TOPPING)
    Observable<RmGetFoodItemExtraToppingResponse> getFoodItemExtraTopping(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("ItemID") String food_ID,
            @Query("FoodItemSizeID") String foodItemSizeID
    );

    @GET(Constant.Url.VERIFY_POSTALCODE)
    Observable<RmVerifiyPostalcodeResponse> verifyPostalCode(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("Order_Type") String order_type,
            @Query("Postcode") String postal_code
    );

    @GET(Constant.Url.TABLE)
    Observable<RmGetTableListResponse> getTableList(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("branch_id") String branch_id
    );

    @GET(Constant.Url.RESTRURENT_DISCOUNT_PRICE)
    Observable<RmGetRestaurantDiscountResponse> getRestaurentDiscountPrice(
            @Query("branch_id") String branch_id,
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("subTotal") String subTotal,
            @Query("Order_Type") String orderType
    );

    @GET(Constant.Url.SERVICE_CHARGE)
    Observable<RmGetServiceChargeResponse> getServiceCharge(
            @Query("branch_id") String branch_id,
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("subTotal") String subTotal
    );

    @GET(Constant.Url.VERIFY_COUPON)
    Observable<RmVerifyCouponCodeResponse> verifyCouponCode(
            @Query("branch_id") String branch_id,
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("subTotal") String subTotal,
            @Query("CouponCode") String couponCode
    );


    @GET(Constant.Url.GET_LOYALTY_POINT)
    Observable<RmGetLoyaltyPointResponse> getTotalLoyaltyPoint(
            @Query("CustomerId") String CustomerId
    );

    @GET(Constant.Url.VERIFY_LOYALTY_POINT)
    Observable<RmVerifyLoyaltyPointResponse> verifyLoyaltyPoint(
            @Query("CustomerId") String CustomerId,
            @Query("loyltPnts") String loyltPnts,
            @Query("TotalFoodCostAmount") String TotalFoodCostAmount
    );

    @GET(Constant.Url.SIGNIN)
    Observable<RmSignInResponse> signIn(
            @Query("user_email") String user_email,
            @Query("user_pass") String user_pass,
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("device_id") String device_id,
            @Query("device_platform") String device_platform
    );

    @GET(Constant.Url.SIGNUP)
    Observable<RmSignUpResponse> signUp(
            @Query("first_name") String first_name,
            @Query("last_name") String last_name,
            @Query("user_email") String user_email,
            @Query("user_pass") String user_pass,
            @Query("user_phone") String user_phone,
            @Query("customer_country") String user_country,
            @Query("referral_code") String referral_code,
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("device_id") String device_id,
            @Query("device_platform") String device_platform
    );

    @GET(Constant.Url.GET_BRANCH)
    Observable<RmGetBranchResponse> getBranch(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code
    );

    @GET(Constant.Url.GET_ADDRESS)
    Observable<RmGetAddressResponse> getAddress(
            @Query("CustomerId") String CustomerId,
            @Query("customer_long") String customer_long,
            @Query("customer_lat") String customer_lat
    );




    @GET(Constant.Url.ADD_ADDRESS)
    Observable<RmAddAddressResponse> addAddress(
            @Query("customerFlat_Name") String flateName,
            @Query("CustomerId") String userid,
            @Query("customerAddressLabel") String addressTitle,
            @Query("customerFloor_House_Number") String house,
            @Query("customerStreet") String street,
            @Query("customerCity") String city,
            @Query("customerState") String state,
            @Query("customerZipcode") String zipcode,
            @Query("customerCountry") String country,
            @Query("address_direction") String address_direction,
            @Query("user_phone") String phone,
            @Query("lang_code") String lang_code,
            @Query("api_key") String api_key
    );

    @GET(Constant.Url.DELETE_ADDRESS)
    Observable<RmDeleteAddressResponse> deleteAddress(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("CustomerAddressId") String addressID
    );

    @GET(Constant.Url.GET_TIME_SLOT)
    Observable<RmGetDeliveryTimeSlotResponse> getTimeSlot(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code
    );

    @GET(Constant.Url.GET_ALLERGY)
    Observable<RmGetAllergyResponse> getAllergy(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code
    );

    @GET(Constant.Url.SEND_ORDER)
    Observable<RmOrderSendToKitchenResponse> orderSendToKitchen(
            @Query("branch_id") String branchId,
            @Query("mealID") String mealID,
            @Query("mealquqntity") String mealquqntity,
            @Query("mealPrice") String mealPrice,
            @Query("itemId") String itemId,
            @Query("Quantity") String Quantity,
            @Query("Price") String Price,
            @Query("strsizeid") String strsizeid,
            @Query("extraItemID") String extraItemID,
            @Query("CustomerId") String CustomerId,
            @Query("order_price") String order_price,
            @Query("subTotalAmount") String subTotalAmount,
            @Query("deliveryCharge") String deliveryCharge,
            @Query("CouponCode") String CouponCode,
            @Query("CouponCodePrice") String CouponCodePrice,
            @Query("couponCodeType") String couponCodeType,
            @Query("SalesTaxAmount") String SalesTaxAmount,
            @Query("extraTipAddAmount") String extraTipAddAmount,
            @Query("discountOfferDescription") String discountOfferDescription,
            @Query("discountOfferPrice") String discountOfferPrice,
            @Query("RestaurantoffrType") String RestaurantoffrType,
            @Query("ServiceFees") String ServiceFees,
            @Query("PaymentProcessingFees") String PaymentProcessingFees,
            @Query("ServiceFeesType") String ServiceFeesType,
            @Query("PackageFeesType") String PackageFeesType,
            @Query("PackageFees") String PackageFees,
            @Query("VatTax") String VatTax,
            @Query("GiftCardPay") String GiftCardPay,
            @Query("WalletPay") String WalletPay,
            @Query("loyptamount") String loyptamount,
            @Query("loyltPnts") String loyltPnts,
            @Query("table_number_assign") String table_number_assign,
            @Query("branch_id") String branch_id,
            @Query("FoodCosts") String FoodCosts,
            @Query("getTotalItemDiscount") String getTotalItemDiscount,
            @Query("getFoodTaxTotal7") String getFoodTaxTotal7,
            @Query("getFoodTaxTotal19") String getFoodTaxTotal19,
            @Query("TotalSavedDiscount") String TotalSavedDiscount,
            @Query("discountOfferFreeItems") String discountOfferFreeItems,
            @Query("number_of_people") String numberOfPeople
    );

    @GET(Constant.Url.SEND_ORDER)
    Observable<RmOrderSendToKitchenResponse> addItemToKitchen(
            @Query("branch_id") String branchId,
            @Query("mealID") String mealID,
            @Query("mealquqntity") String mealquqntity,
            @Query("mealPrice") String mealPrice,
            @Query("itemId") String itemId,
            @Query("Quantity") String Quantity,
            @Query("Price") String Price,
            @Query("strsizeid") String strsizeid,
            @Query("extraItemID") String extraItemID,
            @Query("order_identifyno") String order_identifyno,
            @Query("CustomerId") String CustomerId,
            @Query("order_price") String order_price,
            @Query("subTotalAmount") String subTotalAmount,
            @Query("deliveryCharge") String deliveryCharge,
            @Query("CouponCode") String CouponCode,
            @Query("CouponCodePrice") String CouponCodePrice,
            @Query("couponCodeType") String couponCodeType,
            @Query("SalesTaxAmount") String SalesTaxAmount,
            @Query("extraTipAddAmount") String extraTipAddAmount,
            @Query("discountOfferDescription") String discountOfferDescription,
            @Query("discountOfferPrice") String discountOfferPrice,
            @Query("RestaurantoffrType") String RestaurantoffrType,
            @Query("ServiceFees") String ServiceFees,
            @Query("PaymentProcessingFees") String PaymentProcessingFees,
            @Query("ServiceFeesType") String ServiceFeesType,
            @Query("PackageFeesType") String PackageFeesType,
            @Query("PackageFees") String PackageFees,
            @Query("VatTax") String VatTax,
            @Query("GiftCardPay") String GiftCardPay,
            @Query("WalletPay") String WalletPay,
            @Query("loyptamount") String loyptamount,
            @Query("loyltPnts") String loyltPnts,
            @Query("table_number_assign") String table_number_assign,
            @Query("branch_id") String branch_id,
            @Query("FoodCosts") String FoodCosts,
            @Query("getTotalItemDiscount") String getTotalItemDiscount,
            @Query("getFoodTaxTotal7") String getFoodTaxTotal7,
            @Query("getFoodTaxTotal19") String getFoodTaxTotal19,
            @Query("TotalSavedDiscount") String TotalSavedDiscount,
            @Query("discountOfferFreeItems") String discountOfferFreeItems,
            @Query("number_of_people") String numberOfPeople
    );
    @GET(Constant.Url.GET_CARD_LIST)
    Observable<RmGetCardsResponse> getCardList(
            @Query("branch_id") String branch_id,
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("CustomerId") String CustomerId
    );
    @GET(Constant.Url.PLACE_ORDER)
    Observable<RmPlaceOrderResponse> placeOrder(
            @Query("extraItemIDName1") String extraIdName,
            @Query("extraItemID1") String extraItemId,
            @Query("Extraprice") String extraItemPrice,
            @Query("branch_id") String branch_id,
            @Query("save_card_allow") String save_card_allow,
            @Query("card_type") String card_type,
            @Query("card_number") String card_number,
            @Query("card_expire_month") String card_expire_month,
            @Query("card_expire_year") String card_expire_year,
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("payment_transaction_paypal") String payment_transaction_paypal,
            @Query("mealID") String mealID,
            @Query("mealquqntity") String mealquqntity,
            @Query("mealPrice") String mealPrice,
            @Query("itemId") String itemId,
            @Query("Quantity") String Quantity,
            @Query("Price") String Price,
            @Query("strsizeid") String strsizeid,
            @Query("extraItemID") String extraItemID,
            @Query("CustomerId") String CustomerId,
            @Query("CustomerAddressId") String CustomerAddressId,
            @Query("payment_type") String payment_type,
            @Query("order_price") String order_price,
            @Query("subTotalAmount") String subTotalAmount,
            @Query("delivery_date") String delivery_date,
            @Query("delivery_time") String delivery_time,
            @Query("instructions") String instructions,
            @Query("deliveryCharge") String deliveryCharge,
            @Query("CouponCode") String CouponCode,
            @Query("CouponCodePrice") String CouponCodePrice,
            @Query("couponCodeType") String couponCodeType,
            @Query("SalesTaxAmount") String SalesTaxAmount,
            @Query("order_type") String order_type,
            @Query("SpecialInstruction") String SpecialInstruction,
            @Query("extraTipAddAmount") String extraTipAddAmount,
            @Query("RestaurantNameEstimate") String RestaurantNameEstimate,
            @Query("discountOfferDescription") String discountOfferDescription,
            @Query("discountOfferPrice") String discountOfferPrice,
            @Query("RestaurantoffrType") String RestaurantoffrType,
            @Query("ServiceFees") String ServiceFees,
            @Query("PaymentProcessingFees") String PaymentProcessingFees,
            @Query("deliveryChargeValueType") String deliveryChargeValueType,
            @Query("ServiceFeesType") String ServiceFeesType,
            @Query("PackageFeesType") String PackageFeesType,
            @Query("PackageFees") String PackageFees,
            @Query("WebsiteCodePrice") String WebsiteCodePrice,
            @Query("WebsiteCodeType") String WebsiteCodeType,
            @Query("WebsiteCodeNo") String WebsiteCodeNo,
            @Query("preorderTime") String preorderTime,
            @Query("VatTax") String VatTax,
            @Query("GiftCardPay") String GiftCardPay,
            @Query("WalletPay") String WalletPay,
            @Query("loyptamount") String loyptamount,
            @Query("table_number_assign") String table_number_assign,
            @Query("getFoodTaxTotal7") String getFoodTaxTotal7,
            @Query("getFoodTaxTotal19") String getFoodTaxTotal19,
            @Query("customer_country") String customer_country,
            @Query("group_member_id") String group_member_id,
            @Query("number_of_people") String numberOfPeople,
            @Query("FoodCosts") String FoodCosts
    );

    @GET(Constant.Url.GET_ORDER_LIST)
    Observable<RmGetOrderListResponse> getOrderList(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("CustomerId") String CustomerId
    );

    @GET(Constant.Url.CANCEL_ORDER)
    Observable<RmOrderCancelResponse> cancelOrder(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("order_identifyno") String order_identifyno
    );

    @GET(Constant.Url.TRACK_ORDER)
    Observable<RmTrackOrderResponse> trackOrder(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("order_identifyno") String order_identifyno
    );

    @GET(Constant.Url.GET_ORDER_DETAIL)
    Observable<RmGetOrderDetailResponse> getOrderDetail(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("order_identifyno") String order_identifyno
    );
    @GET(Constant.Url.RE_ORDER)
    Observable<RmTrackOrderResponse> getRReOrder(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("order_identifyno") String order_identifyno
    );
    @GET(Constant.Url.RE_ORDER)
    Observable<RmGetReOrderResponse> getReOrder(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("order_identifyno") String order_identifyno
    );
    @GET(Constant.Url.WRITE_REVIEW)
    Observable<RmUpdateReviewResponse> updateReview(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("order_identifyno") String order_identifyno,
            @Query("CustomerId") String CustomerId,
            @Query("RestaurantReviewRating") String RestaurantReviewRating,
            @Query("Quality_ratingN") String Quality_ratingN,
            @Query("Service_ratingN") String Service_ratingN,
            @Query("Time_ratingN") String Time_ratingN,
            @Query("RestaurantReviewContent") String RestaurantReviewContent
    );

    @GET(Constant.Url.GET_REVIEWS)
    Observable<RmGetReviewsResponse> getReviews(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("CustomerId") String CustomerId
    );

    @GET(Constant.Url.GET_TICKETS)
    Observable<RmGetTicketResponse> getTickets(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("CustomerId") String CustomerId
    );

    @GET(Constant.Url.SUBMIT_TICKET)
    Observable<RmSubmitTicketResponse> submitNewTicket(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("Order_Issue_Subject") String issueSubject,
            @Query("order_identifyno") String orderIdentifyNo,
            @Query("Order_Email_Address") String emailAddress,
            @Query("Order_Message") String message,
            @Query("CustomerId") String CustomerId
    );


    @GET(Constant.Url.GET_RESTAURANT_REVIEW)
    Observable<RmGetRestaurantReviewResponse> getRestaurantReview(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("branch_id") String branch_id
    );

    @GET(Constant.Url.GET_OPENING_HOURS)
    Observable<RmGetOpeningHourResponse> getOpeningHours(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("branch_id")String branch_id
    );

    @GET(Constant.Url.GET_DELIVERY_AREA)
    Observable<RmGetDeliveryAreaResponse> getDeliveryArea(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("splash_type") String splash_type,
            @Query("branch_id")String  branch_id
    );

    @GET(Constant.Url.GET_GALLERY)
    Observable<RmGetGalleryResponse> getGallery(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("splash_type") String splash_type,
            @Query("branch_id")String branch_id
    );

    @GET(Constant.Url.GET_RESTAURANT_OFFERS)
    Observable<RmGetRestaurantOfferResponse> getRestaurantOffers(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("branch_id")String branch_id
    );

    @GET(Constant.Url.SUBMIT_CONTACT_US)
    Observable<RmSubmitContactUsResponse> submitContactUs(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("contact_name") String name,
            @Query("contact_email") String email,
            @Query("contact_mobile") String mobile,
            @Query("contact_message") String message
    );

    @GET(Constant.Url.GET_LOYALTY_POINT_LIST)
    Observable<RmGetLoyaltyPointListResponse> getLoyaltyPointList(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code
    );

    @GET(Constant.Url.GET_PAYMENT)
    Call<RmGetPaymentKeyResponse> getPaymentKey(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code
    );

    @GET(Constant.Url.PAYMENT_BACKEND)
    Call<RmStripeUpdateChargeResponse> updateCharge(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("amount") String amount,
            @Query("currency") String currency,
            @Query("stripeToken") String stripeToken,
            @Query("description") String description
    );

    @GET(Constant.Url.BOOK_A_TABLE)
    Observable<RmBookATableResponse> bookATable(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("CustomerId") String userid,
            @Query("table_number_assign") String tableNumberAssign,
            @Query("booking_mobile") String mobile,
            @Query("booking_date") String bookingDate,
            @Query("booking_time") String bookingtime,
            @Query("booking_instruction") String bookingInstruction,
            @Query("Number_of_person") String noOfPerson,
            @Query("Total_bill_amount") String totalBillAmount,
            @Query("Total_bill_discount_amount") String billDiscountAmount,
            @Query("Total_Sub_Total_after_discount") String subTotalAfterDiscount,
            @Query("Total_Service_Charge") String serviceCharge,
            @Query("Total_Final_Amount") String finalAmount,
            @Query("Total_Deposit_Amount") String depositAmount
    );

    @Multipart
    @POST(Constant.Url.UPDATE_PROFILE)
    Observable<RmUpdateProfileResponse> updateProfile(
            @Query("CustomerId") String userid,
            @Query("CustomerFirstName") String firstName,
            @Query("CustomerLastName") String lastName,
            @Query("CustomerPhone") String mobileNo,
            @Query("customerFloor_House_Number") String houseNo,
            @Query("customerFlat_Name") String flatName,
            @Query("customerStreet") String streetName,
            @Query("customerZipcode") String postalCode,
            @Query("customerCity") String cityName,
            @Part MultipartBody.Part filePar
    );

    @POST(Constant.Url.UPDATE_PROFILE)
    Observable<RmUpdateProfileResponse> updateProfile(
            @Query("CustomerId") String userid,
            @Query("CustomerFirstName") String firstName,
            @Query("CustomerLastName") String lastName,
            @Query("CustomerPhone") String mobileNo,
            @Query("customerFloor_House_Number") String houseNo,
            @Query("customerFlat_Name") String flatName,
            @Query("customerStreet") String streetName,
            @Query("customerZipcode") String postalCode,
            @Query("customerCity") String cityName
    );

    @POST(Constant.Url.GET_PROFILE)
    Observable<RmSignInResponse> getProfile(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("CustomerId") String userid
    );

    @GET(Constant.Url.CHANGE_PASSWORD)
    Observable<RmChangePasswordResponse> changePassword(
            @Query("CustomerId") String userid,
            @Query("OldCustomerPassword") String OldPassword,
            @Query("NewCustomerPassword") String NewPassword
    );

    @GET(Constant.Url.GET_PAY_LATER_ORDER_LIST)
    Observable<RmGetOrderListResponse> getPayLaterOrderList(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("CustomerId") String CustomerId
    );

    @GET(Constant.Url.GET_PAY_LATER_PLACE_ORDER)
    Observable<RmPayLaterPlaceOrderResponse> payLaterPlaceOrder(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("payment_transaction_paypal") String payment_transaction_paypal,
            @Query("mealID") String mealID,
            @Query("mealquqntity") String mealquqntity,
            @Query("mealPrice") String mealPrice,
            @Query("itemId") String itemId,
            @Query("Quantity") String Quantity,
            @Query("Price") String Price,
            @Query("strsizeid") String strsizeid,
            @Query("extraItemID") String extraItemID,
            @Query("CustomerId") String CustomerId,
            @Query("CustomerAddressId") String CustomerAddressId,
            @Query("payment_type") String payment_type,
            @Query("order_price") String order_price,
            @Query("subTotalAmount") String subTotalAmount,
            @Query("delivery_date") String delivery_date,
            @Query("delivery_time") String delivery_time,
            @Query("instructions") String instructions,
            @Query("deliveryCharge") String deliveryCharge,
            @Query("CouponCode") String CouponCode,
            @Query("CouponCodePrice") String CouponCodePrice,
            @Query("couponCodeType") String couponCodeType,
            @Query("SalesTaxAmount") String SalesTaxAmount,
            @Query("order_type") String order_type,
            @Query("SpecialInstruction") String SpecialInstruction,
            @Query("extraTipAddAmount") String extraTipAddAmount,
            @Query("RestaurantNameEstimate") String RestaurantNameEstimate,
            @Query("discountOfferDescription") String discountOfferDescription,
            @Query("discountOfferPrice") String discountOfferPrice,
            @Query("RestaurantoffrType") String RestaurantoffrType,
            @Query("ServiceFees") String ServiceFees,
            @Query("PaymentProcessingFees") String PaymentProcessingFees,
            @Query("deliveryChargeValueType") String deliveryChargeValueType,
            @Query("ServiceFeesType") String ServiceFeesType,
            @Query("PackageFeesType") String PackageFeesType,
            @Query("PackageFees") String PackageFees,
            @Query("WebsiteCodePrice") String WebsiteCodePrice,
            @Query("WebsiteCodeType") String WebsiteCodeType,
            @Query("WebsiteCodeNo") String WebsiteCodeNo,
            @Query("preorderTime") String preorderTime,
            @Query("VatTax") String VatTax,
            @Query("GiftCardPay") String GiftCardPay,
            @Query("WalletPay") String WalletPay,
            @Query("loyptamount") String loyptamount,
            @Query("table_number_assign") String table_number_assign,
            @Query("customer_country") String customer_country,
            @Query("group_member_id") String group_member_id,
            @Query("loyltPnts") String loyltPnts,
            @Query("branch_id") String branch_id,
            @Query("FoodCosts") String FoodCosts,
            @Query("getTotalItemDiscount") String getTotalItemDiscount,
            @Query("getFoodTaxTotal7") String getFoodTaxTotal7,
            @Query("getFoodTaxTotal19") String getFoodTaxTotal19,
            @Query("TotalSavedDiscount") String TotalSavedDiscount,
            @Query("discountOfferFreeItems") String discountOfferFreeItems,
            @Query("order_identifyno") String order_identifyno
    );

    @GET(Constant.Url.GET_PAY_LATER_PLACE_ORDER_DIRECT)
    Observable<RmPayLaterPlaceOrderResponse> payLaterPlaceOrder(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("order_identifyno") String order_identifyno,
            @Query("payment_transaction_paypal") String payment_transaction_paypal,
            @Query("CustomerId") String CustomerId,
            @Query("payment_type") String payment_type,
            @Query("order_price") String order_price
    );

    @GET(Constant.Url.FORGET_PASSWORD)
    Observable<RmForgetPasswordResponse> forgetPassword(
            @Query("api_key") String api_key,
            @Query("lang_code") String lang_code,
            @Query("user_email") String user_email
    );

}
