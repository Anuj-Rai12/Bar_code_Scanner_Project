package com.example.mpos.delete

import android.app.AlertDialog
import android.os.Bundle
import com.example.mpos.delete.unit.Utils
import com.example.mpos.databinding.PrintTsetingLayoutBinding
import com.example.mpos.delete.pine.AppConfig
import com.example.mpos.delete.pine.BasePineActivity
import com.example.mpos.delete.pine.PineServiceHelper
import com.example.mpos.delete.pine.request.Datum
import com.example.mpos.delete.pine.request.Detail
import com.example.mpos.delete.pine.request.Header
import com.example.mpos.delete.pine.request.TransactionRequest
import com.example.mpos.delete.pine.response.TransactionResponse

class PineTestingActivity : BasePineActivity() {
    private lateinit var binding: PrintTsetingLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PrintTsetingLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val psh = PineServiceHelper.getInstance()
        psh.connect(this, this)


        binding.amtByCard.setOnClickListener {
            val amt = 20


            val request = TransactionRequest()
            request.aPP_ID = AppConfig.APP_ID
            //Setting header ;
            val header = Header()
            header.applicationId = AppConfig.APP_ID
            header.methodId = "1001"
            header.userId = "AnujRai101"
            header.versionNo = AppConfig.versionCode
            val detail = Detail()
            detail.data = arrayListOf()
            request.header = header

            //Detail Obj
            detail.billingRefNo = "receiptNo1010"
            detail.paymentAmount = (amt * 100.0).toString()
            detail.transactionType = "4001"//Card
            detail.mobileNumberForEChargeSlip = "9219141756"
            request.detail = detail

            psh.callPineService(request)

        }

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


    }


    private fun itemArr(): ArrayList<Datum> {
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


    override fun sendResult(detailResponse: TransactionResponse?) {
        super.sendResult(detailResponse)
        Utils.createLogcat("PINE_Res", "$detailResponse")
        val dialog=AlertDialog.Builder(this@PineTestingActivity)
        dialog.setTitle("Transaction Message")
        dialog.setMessage("${detailResponse?.response?.responseMsg}")
        dialog.setPositiveButton("ok") { _, _ ->
        }
        dialog.show()
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