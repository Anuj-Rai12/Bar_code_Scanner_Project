package com.example.mpos.payment.pine.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class DetailTrans{
    @SerializedName("TransactionDate")
    public String transactionDate;
    @SerializedName("RetrievalReferenceNumber")
    public String retrievalReferenceNumber;
    @SerializedName("TransactionTime")
    public String transactionTime;
    @SerializedName("HostResponse")
    public String hostResponse;
    @SerializedName("AcquiringBankCode")
    public String acquiringBankCode;
    @SerializedName("BatchNumber")
    public int batchNumber;
    @SerializedName("LoyaltyPointsAwarded")
    public int loyaltyPointsAwarded;
    @SerializedName("MerchantCity")
    public String merchantCity;
    @SerializedName("TerminalId")
    public String terminalId;
    @SerializedName("TransactionType")
    public int transactionType;
    @SerializedName("InvoiceNumber")
    public int invoiceNumber;
    @SerializedName("MerchantName")
    public String merchantName;
    @SerializedName("PrintCardholderName")
    public int printCardholderName;
    @SerializedName("AuthAmoutPaise")
    public String authAmoutPaise;
    @SerializedName("CardEntryMode")
    public String cardEntryMode;
    @SerializedName("Remark")
    public String remark;
    @SerializedName("PosEntryMode")
    public int posEntryMode;
    @SerializedName("MerchantAddress")
    public String merchantAddress;
    @SerializedName("ApprovalCode")
    public String approvalCode;
    @SerializedName("PlutusTransactionLogID")
    public String plutusTransactionLogID;
    @SerializedName("AcquirerName")
    public String acquirerName;
    @SerializedName("MerchantId")
    public String merchantId;
    @SerializedName("CardNumber")
    public String cardNumber;
    @SerializedName("CardType")
    public String cardType;
    @SerializedName("BillingRefNo")
    public String billingRefNo;
    @SerializedName("ExpiryDate")
    public String expiryDate;
    @SerializedName("PlutusVersion")
    public String plutusVersion;
    @SerializedName("Payments")
    public ArrayList<Payment> payments;
}