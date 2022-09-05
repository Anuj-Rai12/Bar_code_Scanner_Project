package com.example.motionlyt

import android.app.Activity
import android.widget.Toast

fun Activity.toastMsg(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun doPrint():String{

    return "<CENTER><BIG>Haldiram's<BR>" +
            "<CENTER><BOLD>Ballia<BR>" +
            "<DLINE><BR>" +
            "<LEFT>BILL NO: 2010240" +
            "<RIGHT>BILL DATE:20/10/2012<BR>" +
            "<LINE><BR>" +
            "<BOLD>Description;;<BOLD>Qty;;<BOLD>Price;;<BOLD>Amount" +
            "<LINE><BR>" +
            "1 This is Item Description;;1;;2999;;20000<BR>" +
            "2 This is Item Description;;2;;2999;;20000<BR>" +
            "3 This is Item Description;;3;;2999;;20000<BR>" +
            "4 This is Item Description;;4;;2999;;20000<BR>" +
            "5 This is Item Description;;5;;2999;;20000<BR>" +
            "<LINE><BR><BR>" +
            "<LEFT>Total item In Est:" +
            "<RIGHT> 300" +
            "<BR><LINE><BR>" +
            "<LEFT>No of Item:" +
            "<RIGHT>200" +
            "<BR><LINE><BR>" +
            "<LEFT>Discount" +
            "<RIGHT>200" +
            "<BR><LINE><BR>" +
            "<LEFT>Total:" +
            "<RIGHT><BOLD>2000" +
            "<BR>" +
            "<DLine><BR><CUT>"
}