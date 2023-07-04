package com.example.mpos.payment.pine;


public class AppConfig {
    public static final Integer PrinterWidth = 32;
    public static final String Separator = String.format("%0" + PrinterWidth + "d", 0).replace("0", "-");

    public static final String REQUEST_KEY = "MASTERAPPREQUEST";
    public static final String RESPONSE_KEY = "MASTERAPPRESPONSE";

    public static final String PINE_ACTION = "com.pinelabs.masterapp.SERVER";
    public static final String PINE_PACKAGE = "com.pinelabs.masterapp";


    public static final String APP_ID = "9fa6692e99b84b38ba2f3abfe51c2397";//"7e9dbc358ddd4fa2ad65a21560916b6b";

    //9fa6692e99b84b38ba2f3abfe51c2397 FBTS PRODUCTION KEY
    //"926148e754c34f86919cafa741de3bc1"; FBTS DEBUG
    public static final String versionCode = "1.0";

    public static final int ITEM_RV = 0;
    public static final int MORE_RV = 1;
    public static final int POSITION_FROM_LAST = 1;

    public static final String UserMno = "1234";
    public static final String UserPassword = "1234";

}
