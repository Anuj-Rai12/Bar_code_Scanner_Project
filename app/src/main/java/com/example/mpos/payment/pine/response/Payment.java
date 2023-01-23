package com.example.mpos.payment.pine.response;

import com.google.gson.annotations.SerializedName;

public class Payment{
    @SerializedName("BillingRefNo") 
    public String billingRefNo;
    @SerializedName("ApprovalCode")
    public String approvalCode;
    @SerializedName("HostResponse") 
    public String hostResponse;
    @SerializedName("CardNumber") 
    public String cardNumber;
    @SerializedName("ExpiryDate") 
    public String expiryDate;
    @SerializedName("CardholderName") 
    public String cardholderName;
    @SerializedName("CardType") 
    public String cardType;
    @SerializedName("InvoiceNumber") 
    public int invoiceNumber;
    @SerializedName("BatchNumber") 
    public int batchNumber;
    @SerializedName("TerminalId") 
    public String terminalId;
    @SerializedName("LoyaltyPointsAwarded") 
    public int loyaltyPointsAwarded;
    @SerializedName("Remark") 
    public String remark;
    @SerializedName("AcquirerName") 
    public String acquirerName;
    @SerializedName("MerchantId") 
    public String merchantId;
    @SerializedName("RetrievalReferenceNumber") 
    public String retrievalReferenceNumber;
    @SerializedName("CardEntryMode") 
    public int cardEntryMode;
    @SerializedName("PrintCardholderName") 
    public int printCardholderName;
    @SerializedName("MerchantName") 
    public String merchantName;
    @SerializedName("MerchantAddress") 
    public String merchantAddress;
    @SerializedName("MerchantCity") 
    public String merchantCity;
    @SerializedName("PlutusVersion") 
    public String plutusVersion;
    @SerializedName("AquiringBankCode") 
    public int aquiringBankCode;
    @SerializedName("TransactionDate") 
    public String transactionDate;
    @SerializedName("TransactionTime") 
    public String transactionTime;
    @SerializedName("PineLabsClientId") 
    public int pineLabsClientId;
    @SerializedName("PineLabsBatchId") 
    public int pineLabsBatchId;
    @SerializedName("PineLabsRoc") 
    public int pineLabsRoc;
}