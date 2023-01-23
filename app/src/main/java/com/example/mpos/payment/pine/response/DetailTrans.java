package com.example.mpos.payment.pine.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DetailTrans{
    @SerializedName("Payments")
    public ArrayList<Payment> payments;
}