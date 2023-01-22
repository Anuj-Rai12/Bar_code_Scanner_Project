package com.example.mpos.payment.pine.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Detail {
    @SerializedName("BillingRefNo")
    public String billingRefNo;
    @SerializedName("Data")
    public ArrayList<Datum> data;
    @SerializedName("MobileNumberForEChargeSlip")
    public String mobileNumberForEChargeSlip;
    @SerializedName("PaymentAmount")
    public String paymentAmount;

    //Status Only
    @SerializedName("InvoiceNo")
    public String invoiceNo;

    @SerializedName("TransactionType")
    public String transactionType;

    //Printing Receipt
    @SerializedName("PrintRefNo")
    public String printRefNo;
    @SerializedName("SavePrintData")
    public Boolean savePrintData;
}
