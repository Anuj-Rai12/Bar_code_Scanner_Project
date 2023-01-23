package com.example.mpos.payment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.example.mpos.R
import com.example.mpos.data.checkBillingStatus.checkstatusedc.PaymentEdcRequest
import com.example.mpos.data.checkBillingStatus.checkstatusedc.PaymentEdcRequestBody
import com.example.mpos.data.table_info.model.json.TableDetail
import com.example.mpos.databinding.PrintTsetingLayoutBinding
import com.example.mpos.payment.pine.AppConfig
import com.example.mpos.payment.pine.BasePineActivity
import com.example.mpos.payment.pine.PineServiceHelper
import com.example.mpos.payment.pine.request.Detail
import com.example.mpos.payment.pine.request.Header
import com.example.mpos.payment.pine.request.TransactionRequest
import com.example.mpos.payment.pine.response.TransactionResponse
import com.example.mpos.payment.unit.Utils
import com.example.mpos.ui.cost.viewmodel.CostDashBoardViewModel
import com.example.mpos.ui.oderconfirm.adaptor.ConfirmOderFragmentAdaptor
import com.example.mpos.ui.oderconfirm.view_model.ConfirmOrderFragmentViewModel
import com.example.mpos.ui.searchfood.model.FoodItemList
import com.example.mpos.utils.*
import com.google.android.material.snackbar.Snackbar

class PaymentActivity : BasePineActivity() {
    private lateinit var binding: PrintTsetingLayoutBinding

    private val confirmOrderViewModel: ConfirmOrderFragmentViewModel by viewModels()
    private val costDashBordViewModel: CostDashBoardViewModel by viewModels()

    private lateinit var confirmOderFragmentAdaptor: ConfirmOderFragmentAdaptor


    private val receipt by lazy {
        intent.getStringExtra("Receipt")
    }

    private val upiCode by lazy {
        intent.getStringExtra("upiCode")
    }

    private val paymentLs by lazy {
        intent.getStringArrayListExtra("payment")
    }

    private val tblNo by lazy {
        intent.getStringExtra("tableNo")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.hide()
        this.changeStatusBarColor(R.color.semi_white_color_two)
        binding = PrintTsetingLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val psh = PineServiceHelper.getInstance()
        psh.connect(this, this)
        createLogStatement("PAY", "RCPT -> $receipt")
        createLogStatement("PAY", "UPI -> $upiCode")
        createLogStatement("PAY", "PAYMENT -> $paymentLs")
        createLogStatement("PAY", "TBL -> $tblNo")

        confirmOrderViewModel.event.observe(this) {
            it.getContentIfNotHandled()?.let { map ->
                if (!map.values.first()) {
                    showSnackBar(map.keys.first(), R.color.color_red)
                } else {
                    showSnackBar(map.keys.first(), R.color.green_color)
                }
            }
        }
        binding.bckArr.setOnClickListener {
            onBackPressed()
        }
        setBtn()
        setRecycleView()

        confirmOrderViewModel.getOccupiedTableItem(
            TableDetail(
                id = 0,
                guestNumber = "",
                status = "",
                tableNo = tblNo ?: "",
                receiptNo = receipt!!,
                billPrinted = false.toString(),
            )
        )

        getAllOrder()
        getGrandTotal()

        //paymentResponse
        getPaymentResponse()

        binding.cashPaymentBtn.setOnClickListener {
            costDashBordViewModel.checkBillingFROMEDCStatus(
                PaymentEdcRequest(
                    PaymentEdcRequestBody(
                        mPosDoc = receipt!!,
                        edcMachineStatus = true,
                        paymentType = "CASH",
                        eDCResponse = ""
                    )
                )
            )
        }

        binding.cardPaymentBtn.setOnClickListener {
            createLogStatement("CARD",
                "Amount is  is ${binding.totalOrderAmt.text.toString().replace(Rs_Symbol, "")}")

            val amt = binding.totalOrderAmt.text.toString().replace(Rs_Symbol, "").toDouble()
            val request = TransactionRequest()
            request.aPP_ID = AppConfig.APP_ID
            //Setting header
            val header = Header()
            header.applicationId = AppConfig.APP_ID
            header.methodId = "1001"
            header.userId = "user_$receipt"
            header.versionNo = AppConfig.versionCode
            val detail = Detail()
            detail.data = arrayListOf()
            request.header = header

            //Detail Obj
            detail.billingRefNo = "receipt_$receipt"
            detail.paymentAmount = (amt * 100.0).toString()
            detail.transactionType = "4001"//Card
            detail.mobileNumberForEChargeSlip = "9219141756"
            request.detail = detail

            psh.callPineService(request)

        }


        /*

        binding.amtByQr.setOnClickListener {

            val amt = 20


            val request = TransactionRequest()
            request.aPP_ID = AppConfig.APP_ID
            //Setting header ;
            val header = Header()
            header.applicationId = AppConfig.APP_ID
            header.methodId = "1001"
            header.userId = "AnujRai101"
            header.versionNo = AppConfig.versionCode
            request.header = header

            //Detail Obj
            val detail = Detail()
            detail.data = arrayListOf()
            detail.billingRefNo = "receiptNo1010"
            detail.paymentAmount = (amt * 100.0).toString()
            detail.transactionType = "5120"
            detail.mobileNumberForEChargeSlip = "9219141756"
            detail.invoiceNo = "0001"
            request.detail = detail
            psh.callPineService(request)
        }

        binding.printData.setOnClickListener {

            val request = TransactionRequest()
            request.aPP_ID = AppConfig.APP_ID
            //Setting header ;
            val header = Header()
            header.applicationId = AppConfig.APP_ID
            header.methodId = "1002"//Print Format
            header.userId = "AnujRai101"
            header.versionNo = AppConfig.versionCode
            request.header = header

            val detail = Detail()
            detail.data = itemArr()
            detail.printRefNo="0103REX"
            detail.savePrintData=true
            request.detail=detail


            psh.callPineService(request)
        }

*/

    }

    private fun getPaymentResponse() {
        costDashBordViewModel.checkBillingFromEdcStatus.observe(this) {
            if (it != null) when (it) {
                is ApisResponse.Error -> {
                    hidePb()
                    if (it.data == null) {
                        it.exception?.localizedMessage?.let { msg ->
                            showErrorDialog(msg)
                        }
                    } else {
                        showErrorDialog("${it.data}")
                    }
                }
                is ApisResponse.Loading -> showPb("${it.data}")
                is ApisResponse.Success -> {
                    hidePb()
                    showErrorDialog("Working Fine", "Success", R.drawable.ic_success)
                    /*viewModel.getPrintBillInvoiceResponse(
                        PrintInvoiceRequest(
                            PrintInvoiceRequestBody("${it.data}")
                        )
                    )*/
                }
            }
        }
    }


    private fun hidePb() {
        binding.pbLayout.root.hide()
    }

    private fun showPb(msg: String) {
        binding.pbLayout.titleTxt.text = msg
        binding.pbLayout.root.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getAllOrder() {
        confirmOrderViewModel.occupiedTbl.observe(this) {
            when (it) {
                is ApisResponse.Error -> {
                    hidePb()
                    if (it.data == null) {
                        it.exception?.localizedMessage?.let { err ->
                            showErrorDialog(err)
                        }
                    } else {
                        showErrorDialog("${it.data}")
                    }
                }
                is ApisResponse.Loading -> {
                    showPb("${it.data}")
                }
                is ApisResponse.Success -> {
                    hidePb()
                    binding.orderRecycleView.show()
                    (it.data as FoodItemList?)?.let { item ->
                        confirmOrderViewModel.getGrandTotal(item.foodList)
                        if (item.foodList.isNotEmpty()) {
                            confirmOderFragmentAdaptor.notifyDataSetChanged()
                            confirmOderFragmentAdaptor.submitList(item.foodList)
                        } else msg("No order found!! ${getEmojiByUnicode(0x1F615)}")
                    }
                }
            }
        }
    }

    private fun setBtn() {
        if (!paymentLs.isNullOrEmpty()) {
            binding.cardPaymentBtn.hide()
            binding.upiPaymentBtn.hide()
            binding.cashPaymentBtn.hide()
            paymentLs?.forEach {
                if (it.equals("Card", true)) {
                    binding.cardPaymentBtn.show()
                    binding.totalOrderAmt.updateLayoutParams<ConstraintLayout.LayoutParams> {
                        bottomToTop = binding.cardPaymentBtn.id
                    }
                }
                if (it.equals("upi", true)) {
                    binding.upiPaymentBtn.show()
                    binding.totalOrderAmt.updateLayoutParams<ConstraintLayout.LayoutParams> {
                        bottomToTop = binding.upiPaymentBtn.id
                    }
                }
                if (it.equals("cash", true)) {
                    binding.cashPaymentBtn.show()
                    binding.totalOrderAmt.updateLayoutParams<ConstraintLayout.LayoutParams> {
                        bottomToTop = binding.cashPaymentBtn.id
                    }
                }
            }
        }
    }


    private fun showErrorDialog(
        msg: String,
        type: String = "Failed",
        ic: Int = R.drawable.ic_error
    ) {
        val dialog = AlertDialog.Builder(this@PaymentActivity)
        dialog.setTitle(type)
        dialog.setMessage(msg)
        dialog.setIcon(ic)
        dialog.setPositiveButton("ok") { _, _ ->
        }
        dialog.show()
    }

    private fun setRecycleView() {
        binding.orderRecycleView.apply {
            confirmOderFragmentAdaptor = ConfirmOderFragmentAdaptor({}, {})
            confirmOderFragmentAdaptor.setCheckBoxType(false)
            adapter = confirmOderFragmentAdaptor
        }
    }


    /* private fun itemArr(): ArrayList<Datum> {
         val arr= ArrayList<Datum>()
         for (i in 1 until 3) {
             val datum = Datum()
             datum.imageData = "0"
             datum.dataToPrint = "Print the line is $i"
             datum.imagePath="0"
             datum.printDataType = "0"
             datum.printerWidth=AppConfig.PrinterWidth
             datum.isCenterAligned=true
             arr.add(datum)
             arr.add(line())
         }
         return arr
     }

     private fun line(): Datum {
         val datum = Datum()
         datum.imageData = "0"
         datum.dataToPrint = "--------------------------------"
         datum.imagePath = "0"
         datum.printDataType = "0"
         datum.isCenterAligned = true
         datum.printerWidth = AppConfig.PrinterWidth
         return datum
     }
 */
    private fun getGrandTotal() {
        confirmOrderViewModel.grandTotal.observe(this) {
            binding.totalOrderAmt.text = it
        }
    }

    private fun showSnackBar(msg: String, color: Int, length: Int = Snackbar.LENGTH_SHORT) {
        binding.root.showSandbar(
            msg, length, getColorInt(color)
        ) {
            return@showSandbar "OK"
        }
    }

    override fun sendResult(detailResponse: TransactionResponse?) {
        super.sendResult(detailResponse)
        detailResponse?.let {transactionResponse ->
            if (transactionResponse.response.responseCode==0){ //Success
                Utils.createLogcat("PINE_SUC","$transactionResponse")
            }else{
                showErrorDialog(transactionResponse.response.responseMsg)
            }
        }?:showErrorDialog("Oops Something Went Wrong")
    }

    override fun showToast(msg: String?) {
        Utils.createLogcat("PINE_Res", "$msg")
    }

    override fun connectAgain() {
        Utils.createLogcat("PINE_Res", "Connect Again")
    }

    override fun showWaitingDialog() {
        Utils.createLogcat("PINE_Res", "Waiting...")
    }

}