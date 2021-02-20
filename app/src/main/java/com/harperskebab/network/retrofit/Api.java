package com.harperskebab.network.retrofit;

import com.harperskebab.model.AddressFromPostcode;
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

public interface Api {
    String BASE_URL = "https://api.getaddress.io/";


    @GET("find/BN31AE")
    Observable<AddressFromPostcode> getAddressFromPostCode(
            @Query("api-key") String api_key
    );


}
