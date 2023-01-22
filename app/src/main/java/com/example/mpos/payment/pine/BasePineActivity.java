package com.example.mpos.payment.pine;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mpos.payment.pine.response.TransactionResponse;


public abstract class BasePineActivity extends AppCompatActivity implements PineServiceHelper.PineCallBack {
    @Override
    protected void onRestart() {
        super.onRestart();
        PineServiceHelper.getInstance().connect(this, this);
    }

    @Override
    public void showToast(String msg) {
        //UIUtils.dismissProgressbar();
        // UIUtils.makeToast(this, msg);
    }

    @Override
    public void connectAgain() {
        //  UIUtils.dismissProgressbar();
        PineServiceHelper.getInstance().connect(this, this);
    }

    @Override
    public void sendResult(TransactionResponse detailResponse) {
        // UIUtils.dismissProgressbar();
        Log.i("TRANS_Amt", "sendResult: Response" + detailResponse);

        //String userId = detailResponse.getHeader().getUserId();


        //detailResponse.getHeader().getMethodId();

        //  System.out.println(" BILLING_APP >> value >> "+GsonUtils.fromJsonToString(detailResponse));

        //   System.out.println(" detailResponse user id  >> "+detailResponse.getHeader().getUserId());

        //   System.out.println(" detailResponse code  >> "+detailResponse.getResponse().getResponseCode());

        //   System.out.println(" detailResponse getMethodId  >> "+detailResponse.getHeader().getMethodId());

        //     System.out.println("inside response >>>> >>>  >>>  "+userId);

/*        if("1001".equals(methoId)){


            HistoryDetailsActivity1 activity1 = (HistoryDetailsActivity1) this;

            if (NetworkCall.isConnectingToInternet(activity1)) {

                HashMap<String, String> postData = new HashMap<String, String>();

                postData.put("Type" , "Perform");
                postData.put("Checkout_Type" , "POS_STATUS");
                postData.put("TRANS_ID" , detailResponse.getHeader().getUserId().split("-")[1]);

                postData.put("Booking_Id" , detailResponse.getHeader().getUserId().split("-")[1].substring(8,15));

                postData.put("PAY_MODE" , "Online");

                if(detailResponse.getResponse().getResponseCode() == 0){
                    postData.put("TRANS_STATUS" , "success");
                }else{
                    postData.put("TRANS_STATUS" , "failure");
                }

                postData.put("TRANS_RESPONSE" , detailResponse.getResponse().getResponseMsg());

                String url = Services.CHECK_OUT;

                if (url == "") return ;

                new NetworkCall(activity1, url, postData, new HashMap<String,String>(), true,
                        (response1, statusCode) -> {

                            try{

                             //   System.out.println("response1  >> "+response1);

                                if(response1.length() == 0){
                                    throw new Exception("No Response");
                                }

                                JSONObject jsonObject = new JSONObject(response1);
                                String status = jsonObject.getString("Status");
                                String message = jsonObject.getString("Message");

                                if(status.equals("1")){

                                    activity1.successPayment();

                                }else{

                                    activity1.failurePayment();

                                }


                            } catch ( Exception e) {
                                e.printStackTrace();

                                activity1.failurePayment();

                            }

                        });

            }

        }

   //     System.out.println("above print response >>>> >>>  >>>  "+methoId);

        if("1002".equals(methoId) && !"history".equals(userId)){

            HistoryDetailsActivity1 activity1 = (HistoryDetailsActivity1) this;

          //  System.out.println("above printSuccess >>>> >>>  >>>  "+userId);

    activity1.printSuccess(userId);

        }*/

    }

    @Override
    public void showWaitingDialog() {
        //UIUtils.showDialog(this, "Please Wait...");
    }

}

