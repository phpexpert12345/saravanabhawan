package com.harperskebab.utils;

public class Constant {

    public static class Url {
                public static String BASE_URL = "https://dmd.foodsdemo.com/restaurantAPI/";
//        public static String BASE_URL = "http://kartika.takeaway-website.com/restaurantAPI/";
        public static final String LOCATION = "phpexpert_app_open_location.php";
        public static final String LANGUAGE = "phpexpert_customer_app_langauge.php";
        public static final String LANGUAGE_LIST = "phpexpert_customer_app_langauge_list.php";
        public static final String SPLASH = "phpexpert_restaurant_app_splash_gallery.php";
        public static final String RESTRURENT_INFORMATION = "phpexpert_restaurant_information.php";
        public static final String HOME_SLIDER = "phpexpert_home_api.php";
        public static final String FOOD_CATEGORY = "phpexpert_restaurantMenuCategory.php";
        public static final String FOOD = "phpexpert_food_items.php";
        public static final String FOOD_COMBO = "phpexpert_combo_list.php";
        public static final String FOOD_ITEM = "phpexpert_restaurantMenuItemSize.php";
        public static final String FOOD_ITEM_EXTRA_TOPPING = "phpexpert_food_items_extra.php";
        public static final String FOOD_COMBO_ITEM_EXTRA_TOPPING = "phpexpert_food_combo_items_extra.php";
        public static final String VERIFY_POSTALCODE = "phpexpert_postcode_validator.php";
        public static final String TABLE = "phpexpert_table_list.php";
        public static final String RE_ORDER = "phpexpert_ReOrder_App.php";
        public static final String RESTRURENT_DISCOUNT_PRICE = "discountGet.php";
        public static final String SERVICE_CHARGE = "ServiceChargetGet.php";
        public static final String VERIFY_COUPON = "couponCode.php";
        public static final String GET_LOYALTY_POINT = "phpexpert_customer_loyalty_point.php";
        public static final String VERIFY_LOYALTY_POINT = "phpexpert_customer_loyalty_point_redeem.php";
        public static final String SIGNIN = "phpexpert_customer_login.php";
        public static final String SIGNUP = "phpexpert_customer_account_register.php";
        public static final String GET_BRANCH = "phpexpert_get_restaurant_branch_list.php";
        public static final String GET_ADDRESS = "phpexpert_customer_address_list.php";
        public static final String GET_CARD_LIST = "phpexpert_customer_card_list.php";
        public static final String ADD_ADDRESS = "phpexpert_customer_add_new_address.php";
        public static final String DELETE_ADDRESS = "customer_address_delete.php";
        public static final String GET_TIME_SLOT = "phpexpert_time_fetch.php";
        public static final String GET_ALLERGY = "phpexpert_restaurant_allery_info.php";
        public static final String SEND_ORDER = "phpexpert_payment_Eat_In_Order.php";
        public static final String PLACE_ORDER = "phpexpert_payment_android_submit.php";
        public static final String GET_ORDER_LIST = "phpexpert_OrderDisplay.php";
        public static final String TRACK_ORDER = "phpexpert_Order_Track_Dettail.php";
        public static final String CANCEL_ORDER = "phpexpert_Customer_Order_Cancelled.php";
        public static final String GET_ORDER_DETAIL = "phpexpert_Order_DetailsDisplay.php";
        public static final String WRITE_REVIEW = "phpexpert_write_review.php";
        public static final String GET_REVIEWS = "phpexpert_CustomerReview.php";
        public static final String GET_TICKETS = "phpexpert_customer_ticket_list.php";
        public static final String SUBMIT_TICKET = "phpexpert_customer_ticket_submit.php";
        public static final String GET_RESTAURANT_REVIEW = "phpexpert_restaurantReview.php";
        public static final String GET_OPENING_HOURS = "phpexpert_restaurant_opening_hours.php";
        public static final String GET_DELIVERY_AREA = "phpexpert_get_restaurant_delivery_list.php";
        public static final String GET_GALLERY = "phpexpert_food_gallery.php";
        public static final String GET_RESTAURANT_OFFERS = "phpexpert_restaurant_Offers.php";
        public static final String SUBMIT_CONTACT_US = "phpexpert_customer_contact_submit.php";
        public static final String GET_LOYALTY_POINT_LIST = "phpexpert_restaurant_loyalty_point_list.php";
        public static final String GET_PAYMENT = "phpexpert_payment_key.php";
        public static final String PAYMENT_BACKEND = "phpexpert_payment_intent_generate.php";
        public static final String BOOK_A_TABLE = "phpexpert_customer_table_booking.php";
        public static final String UPDATE_PROFILE = "phpexpert_customer_profite_update.php";
        public static final String GET_PROFILE = "phpexpert_customer_profile_inform.php";
        public static final String CHANGE_PASSWORD = "phpexpert_customer_passwordChange.php";
        public static final String GET_PAY_LATER_ORDER_LIST = "phpexpert_OrderDisplayPayLater.php";
        public static final String GET_PAY_LATER_PLACE_ORDER = "phpexpert_payment_pay_later.php";
        public static final String GET_PAY_LATER_PLACE_ORDER_DIRECT = "phpexpert_payment_pay_later_direct.php";
        public static final String FORGET_PASSWORD = "phpexpert_customer_forgot_password.php";
    }

    public static class Header {
        public static final String AUTH_ID = "Authorization";
    }

    public static class Data {
        public static final String CONTAINER_ID = "CONTAINER_ID";
        public static final String FOOD_CATEGORY_POSITION = "FOOD_CATEGORY_POSITION";
        public static final String FOOD = "FOOD";
        public static final String FOOD_ITEM = "FOOD_ITEM";
        public static final String ORDER_ID = "ORDER_ID";
        public static final String FOOD_IMAGE_LINK = "FOOD_IMAGE_LINK";
        public static final String CUSTOMER_NAME = "CUSTOMER_NAME";
        public static final String CUSTOMER_MOBILE = "CUSTOMER_MOBILE";
        public static final String BOOKING_DATE = "BOOKING_DATE";
        public static final String BOOKING_TIME = "BOOKING_TIME";
        public static final String SPECIAL_INSTRUCTIONS = "SPECIAL_INSTRUCTIONS";
        public static final String TABLE = "TABLE";
        public static final String NO_OF_PERSON = "NO_OF_PERSON";
        public static final String CURRENT_PHOTO_PATH = "CURRENT_PHOTO_PATH";
        public static final String IS_CONTINUE_ORDER_VISIBLE = "IS_CONTINUE_ORDER_VISIBLE";
        public static final String SELECTED_TABLE_ID = "SELECTED_TABLE_ID";
    }

    public static class API {
        public static String FOOD_KEY = "foodkey";
        public static String LANGUAGE_CODE = "en";
        public static final String SLIDER = "slider";
    }

    public static class SharedPreference {
        public static final String ORDER_TYPE = "ORDER_TYPE";
        public static final String POSTAL_CODE = "POSTAL_CODE";
        public static final String TABLE_ID = "TABLE_ID";
        public static final String USER = "USER";
        public static final String SELECTED_FOOD = "SELECTED_FOOD";
        public static final String EAT_IN_ORDER_ID = "EAT_IN_ORDER_ID";
        public static final String COUPON_CODE = "COUPON_CODE";
        public static final String COUPON_CODE_DISCOUNT = "COUPON_CODE_DISCOUNT";
        public static final String LOYALTY_CODE_DISCOUNT = "LOYALTY_CODE_DISCOUNT";
        public static final String RESTAURANT_DISCOUNT = "RESTAURANT_DISCOUNT";
        public static final String FOOD_TAX = "FOOD_TAX";
        public static final String DRINK_TAX = "DRINK_TAX";
        public static final String RIDER_TIP = "RIDER_TIP";
        public static final String SUB_TOTAL = "SUB_TOTAL";
        public static final String TO_PAY = "TO_PAY";
        public static final String SELECTED_RESTAURANT_BRANCH = "SELECTED_RESTAURANT_BRANCH";
        public static final String SELECTED_DELIVERY_ADDRESS = "SELECTED_DELIVERY_ADDRESS";
        public static final String ORDER_SEND_TO_KITCHEN_RESPONSE = "ORDER_SEND_TO_KITCHEN_RESPONSE";
        public static final String ORDERED_FOOD = "ORDERED_FOOD";
    }
}
