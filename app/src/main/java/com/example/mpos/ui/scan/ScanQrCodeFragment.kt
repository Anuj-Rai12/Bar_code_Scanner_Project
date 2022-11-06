package com.example.mpos.ui.scan

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mpos.R
import com.example.mpos.data.barcode.response.json.BarcodeJsonResponse
import com.example.mpos.data.crosssellingApi.response.json.CrossSellingJsonResponse
import com.example.mpos.data.item_master_sync.json.ItemMaster
import com.example.mpos.data.login.model.TestingBarcodeConnection
import com.example.mpos.databinding.ScanQrLayoutBinding
import com.example.mpos.ui.crosselling.CrossSellingDialog
import com.example.mpos.ui.menu.repo.OnBottomSheetClickListener
import com.example.mpos.ui.scan.utils.LuminosityAnalyzer
import com.example.mpos.ui.scan.viewmodel.BarCodeViewModel
import com.example.mpos.ui.searchfood.adaptor.ListOfFoodItemToSearchAdaptor
import com.example.mpos.ui.searchfood.model.FoodItemList
import com.example.mpos.ui.searchfood.model.ItemMasterFoodItem
import com.example.mpos.ui.searchfood.view_model.SearchFoodViewModel
import com.example.mpos.utils.*
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.ArrayList


class ScanQrCodeFragment : Fragment(R.layout.scan_qr_layout), OnBottomSheetClickListener {
    private lateinit var binding: ScanQrLayoutBinding
    private var imageCapture: ImageCapture? = null
    private var flagList: Boolean = false
    private lateinit var cameraExecutor: ExecutorService
    private val args: ScanQrCodeFragmentArgs by navArgs()
    private var showDialog = true
    private val viewModel: BarCodeViewModel by viewModels()

    private val searchViewModel: SearchFoodViewModel by viewModels()

    private var crossSellingBarcodeJsonResponse: BarcodeJsonResponse? = null

    private val option = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_AZTEC,
            Barcode.FORMAT_CODE_93,
            Barcode.FORMAT_CODABAR,
            Barcode.FORMAT_CODE_128,
            Barcode.FORMAT_CODE_39,
            Barcode.FORMAT_EAN_8,
            Barcode.FORMAT_ITF,
            Barcode.FORMAT_DATA_MATRIX,
            Barcode.FORMAT_EAN_13,
            Barcode.FORMAT_PDF417,
            Barcode.FORMAT_UPC_A,
            Barcode.FORMAT_UPC_E,
        )
        .build()


    private val scanner by lazy {
        //BarcodeScanning.getClient(option)
        BarcodeScanning.getClient()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().changeStatusBarColor(R.color.black)
        binding = ScanQrLayoutBinding.bind(view)
        cameraExecutor = Executors.newSingleThreadExecutor()
        (activity as com.example.mpos.MainActivity?)?.getPermission()
        viewModel.events.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { res ->
                if (res is ApisResponse.Error) {
                    showSnackBar("${res.data}")
                }
            }
        }

        searchViewModel.events.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { res ->
                showErrorDialog(res)
            }
        }

        barCodeResult()
        getCrossSellingResponse()
        startCamera()
    }

    private fun getCrossSellingResponse() {
        searchViewModel.crossSellingResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> {
                    if (it.data != null) {
                        showErrorDialog("${it.data}")
                    } else {
                        it.exception?.localizedMessage?.let { exp ->
                            showErrorDialog(exp)
                        }
                    }
                    showLoadingSrc(false)
                    showScannerScreen(true)
                }
                is ApisResponse.Loading -> {
                    showLoadingSrc(true, "${it.data}")
                    showScannerScreen(false)
                }
                is ApisResponse.Success -> {
                    showLoadingSrc(false)
                    val res = it.data as CrossSellingJsonResponse
                    openCrossSellingDialog(res)
                }
            }
        }
    }


    private fun barCodeResult() {
        viewModel.barCodeResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                when (it) {
                    is ApisResponse.Error -> {
                        showLoadingSrc(false)
                        if (it.data == null) {
                            it.exception?.localizedMessage?.let { err ->
                                showErrorDialog(err)
                            }
                        } else {
                            showErrorDialog("${it.data}")
                        }
                    }
                    is ApisResponse.Loading -> {
                        showLoadingSrc(true, "${it.data}")
                        showScannerScreen(false)
                    }
                    is ApisResponse.Success -> {
                        showLoadingSrc(false)
                        showScannerScreen(false)
                        (it.data as BarcodeJsonResponse?)?.let { res ->
                            crossSellingBarcodeJsonResponse = res
                            val flag =
                                res.crossSellingAllow.lowercase(Locale.getDefault()).toBoolean()
                            if (flag) {
                                searchViewModel.getCrossSellingItem(res.itemCode)
                            } else {
                                goToNextScreenConfirmScr(res)
                            }
                        } ?: showErrorDialog("Some thing Went Wrong")
                    }
                }
            }
        }
    }

    private fun showErrorDialog(msg: String) {
        showDialogBox(
            "Failed!!",
            msg,
            icon = R.drawable.ic_error,
            isCancel = false
        ) {
            showScannerScreen(true)
            flagList = false
            //  showErrorDialogOnce = true
        }
        //showErrorDialogOnce = false
    }


    private fun showLoadingSrc(flag: Boolean, text: String? = null) {
        if (flag) {
            binding.pbLayout.titleTxt.setTextColor(ColorStateList.valueOf(Color.GREEN))
            binding.pbLayout.titleTxt.text = text
            binding.pbLayout.root.show()
        } else {
            binding.pbLayout.root.hide()
        }
    }

    private fun showScannerScreen(flag: Boolean) {
        if (flag) {
            binding.scanQrLayout.show()
        } else {
            binding.scanQrLayout.hide()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity?.applicationContext!!)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .build()

            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(
                        cameraExecutor, LuminosityAnalyzer({ luma ->
                            Log.d("TAG", "Average luminosity: $luma")
                        }, { imageInput ->
                            scanner.process(imageInput).addOnSuccessListener { list ->
                                if (!list.isNullOrEmpty() && !flagList) {
                                    if (list.first().valueType == args.item) {
                                        flagList = true
                                        sendData(list.first())
                                    } else if (list.last().valueType == args.item) {
                                        flagList = true
                                        sendData(list.last())
                                    }
                                    return@addOnSuccessListener
                                }

                            }.addOnFailureListener { e ->
                                Log.i(TAG, "startCamera: Error is $e")
                            }
                        })
                    )
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalyzer
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(activity?.applicationContext!!))
    }

    private fun sendData(first: Barcode) {
        activity?.msg("Qr Code Detected")
        val type = WhereToGoFromScan.TESTINGCONNECTION.name
        if (args.item == Url_Text && args.type != type) {
            first.rawValue?.let {
                viewModel.checkForItemItem(it, args.type)
            } ?: activity?.msg("Oops SomeThing Went Wrong")
        } else {
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                if (showDialog) {
                    showDialog = false
                    val barcodeValue =
                        TestingBarcodeConnection(title = first.url?.title, uri = first.url?.url)
                    val action =
                        ScanQrCodeFragmentDirections.actionScanQrCodeFragmentToTestingConnectionFragment(
                            barcodeValue
                        )
                    findNavController().navigate(action)
                }
            }
        }
    }


    private fun showSnackBar(msg: String, length: Int = Snackbar.LENGTH_INDEFINITE) {
        binding.root.showSandbar(
            msg,
            length,
            requireActivity().getColorInt(R.color.color_red)
        ) {
            return@showSandbar "OK"
        }
    }


    private fun goToNextScreenConfirmScr(
        barcode: BarcodeJsonResponse,
        crossSellingJsonResponse: Pair<Double, CrossSellingJsonResponse>? = null
    ) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            if (showDialog) {
                showDialog = false
                val arr = ArrayList<ItemMasterFoodItem>()
                args.food?.let { item ->
                    arr.addAll(item.foodList)
                }
                val itemMaster = ItemMaster(
                    barcode = barcode.barcode,
                    id = 0,
                    itemCategory = barcode.itemCategory,
                    salePrice = barcode.salePrice,
                    itemDescription = barcode.itemDescription,
                    itemCode = barcode.itemCode,
                    itemName = barcode.itemName,
                    uOM = barcode.uOM,
                    decimalAllowed = barcode.decimalAllowed,
                    crossSellingAllow = barcode.crossSellingAllow
                )
                itemMaster.foodQty = barcode.qty.toDouble()
                val amt =
                    (ListOfFoodItemToSearchAdaptor.setPrice(itemMaster.salePrice) * itemMaster.foodQty)
                itemMaster.foodAmt =
                    "%.4f".format(amt).toDouble() + (crossSellingJsonResponse?.first ?: 0.0)

                Log.i("QR", "sendData: $itemMaster")
                arr.add(
                    ItemMasterFoodItem(
                        itemMaster = itemMaster,
                        foodQty = itemMaster.foodQty,
                        foodAmt = itemMaster.foodAmt,
                        crossSellingItems = crossSellingJsonResponse?.second,
                        bg = if (crossSellingJsonResponse != null)
                            listOfBg[2]
                        else
                            listOfBg.first()
                    )
                )
                val action = when (WhereToGoFromScan.valueOf(args.type)) {
                    WhereToGoFromScan.TESTINGCONNECTION -> null
                    WhereToGoFromScan.TABLEMANGMENT -> {
                        ScanQrCodeFragmentDirections.actionScanQrCodeFragmentToConfirmOderFragment(
                            FoodItemList(arr),
                            args.tbl!!,
                            args.confirmreq
                        )
                    }
                    WhereToGoFromScan.COSTESTIMATE -> {
                        ScanQrCodeFragmentDirections.actionScanQrCodeFragmentToCostDashBoardFragment(
                            FoodItemList(arr),
                            args.confirmreq
                        )
                    }
                    WhereToGoFromScan.SHOWROOMESTIMATE -> {
                        ScanQrCodeFragmentDirections.actionScanQrCodeFragmentToShowRoomEstimationFragment(
                            FoodItemList(arr),
                            args.confirmreq
                        )
                    }
                    WhereToGoFromScan.RESTAURANTESTIMATE -> {
                        ScanQrCodeFragmentDirections.actionScanQrCodeFragmentToRestaurantEstimationFragments(
                            FoodItemList(arr),
                            args.confirmreq
                        )
                    }
                    WhereToGoFromScan.BILLPAYMENT -> {
                        ScanQrCodeFragmentDirections.actionScanQrCodeFragmentToBillingFragment(
                            FoodItemList(arr),
                            args.confirmreq
                        )
                    }
                    WhereToGoFromScan.SHOWROOMBILLING -> {
                        ScanQrCodeFragmentDirections.actionScanQrCodeFragmentToShowRoomBillingFragment(
                            FoodItemList(arr),
                            args.confirmreq
                        )
                    }
                    WhereToGoFromScan.RESTARURANTBILLING -> {
                        ScanQrCodeFragmentDirections.actionScanQrCodeFragmentToRestaurantBillingFragment(
                            FoodItemList(arr),
                            args.confirmreq
                        )
                    }
                }

                action?.let { findNavController().navigate(it) } ?: run {
                    showErrorDialog("Cannot Go Back")
                }
            }
        }
    }

    private fun openCrossSellingDialog(response: CrossSellingJsonResponse) {
        val dialog = CrossSellingDialog(activity!!)
        dialog.itemClicked = this
        dialog.showCrossSellingDialog(response)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> onItemClicked(response: T) {
        val res = response as Pair<Double, CrossSellingJsonResponse>
        crossSellingBarcodeJsonResponse?.let {
            goToNextScreenConfirmScr(it, res)
        } ?: showErrorDialog("Cannot find BarcodeResponse")
    }

}