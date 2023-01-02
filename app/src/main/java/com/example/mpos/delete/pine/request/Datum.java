package com.example.mpos.delete.pine.request;

import com.google.gson.annotations.SerializedName;

public class Datum{
    @SerializedName("DataToPrint") 
    public String dataToPrint;
    @SerializedName("ImageData")
    public String imageData;
    @SerializedName("ImagePath") 
    public String imagePath;
    @SerializedName("IsCenterAligned") 
    public boolean isCenterAligned;
    @SerializedName("PrintDataType") 
    public String printDataType;
    @SerializedName("PrinterWidth") 
    public int printerWidth;
}

