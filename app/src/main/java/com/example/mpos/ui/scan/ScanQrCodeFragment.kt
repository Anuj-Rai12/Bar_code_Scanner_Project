package com.example.mpos.ui.scan

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.example.mpos.R
import com.example.mpos.data.barcode.response.json.BarcodeJsonResponse
import com.example.mpos.data.crosssellingApi.response.json.CrossSellingJsonResponse
import com.example.mpos.data.item_master_sync.json.ItemMaster
import com.example.mpos.data.login.model.TestingBarcodeConnection
import com.example.mpos.databinding.ScanQrLayoutBinding
import com.example.mpos.ui.crosselling.CrossSellingDialog
import com.example.mpos.ui.menu.repo.OnBottomSheetClickListener
import com.example.mpos.ui.scan.viewmodel.BarCodeViewModel
import com.example.mpos.ui.searchfood.adaptor.ListOfFoodItemToSearchAdaptor
import com.example.mpos.ui.searchfood.model.FoodItemList
import com.example.mpos.ui.searchfood.model.ItemMasterFoodItem
import com.example.mpos.ui.searchfood.view_model.SearchFoodViewModel
import com.example.mpos.utils.*
import com.google.android.material.snackbar.Snackbar
import java.util.*


class ScanQrCodeFragment : Fragment(R.layout.scan_qr_layout), OnBottomSheetClickListener {
    private lateinit var binding: ScanQrLayoutBinding
    private var codeScanner: CodeScanner? = null

    //private var imageCapture: ImageCapture? = null
//    private var flagList: Boolean = false
    private var progressDialog: ProgressDialog? = null

    //private lateinit var cameraExecutor: ExecutorService
    private val args: ScanQrCodeFragmentArgs by navArgs()
    private var showDialog = true
    private val viewModel: BarCodeViewModel by viewModels()

    private val searchViewModel: SearchFoodViewModel by viewModels()

    private var crossSellingBarcodeJsonResponse: BarcodeJsonResponse? = null

    /* private val option = BarcodeScannerOptions.Builder()
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
         .build()*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.getString("REST_INS")?.let {
            RestaurantSingletonCls.getInstance().setScreenType(it)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("REST_INS", RestaurantSingletonCls.getInstance().getScreenType())
    }

    /*   private val scanner by lazy {
           //BarcodeScanning.getClient(option)
           BarcodeScanning.getClient()
       }*/


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().changeStatusBarColor(R.color.black)
        binding = ScanQrLayoutBinding.bind(view)
        //cameraExecutor = Executors.newSingleThreadExecutor()
        //(activity as com.example.mpos.MainActivity?)?.getPermission()
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
        startProcess()
    }

    private fun startProcess() {
        if (args.selectioncls == null || args.selectioncls!!.isBarcodeVisible) {
            // if Barcode is True then only show
            startCamera()
        } else {
            // show test box to get input
            activity?.getDialogInput("Enter Item Code", false, binding.root, { code ->
                viewModel.checkForItemItem(code, args.type)
            }, {
                findNavController().popBackStack()
            })
        }
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
            startProcess()
            //flagList = false
            //  showErrorDialogOnce = true
        }
        //showErrorDialogOnce = false
    }


    private fun showLoadingSrc(flag: Boolean, text: String? = null) {
        if (flag) {
            progressDialog = ProgressDialog(activity)
            progressDialog?.setMessage(text)
            progressDialog?.setCancelable(false)
            progressDialog?.show()
//            binding.pbLayout.titleTxt.setTextColor(ColorStateList.valueOf(Color.GREEN))
//            binding.pbLayout.titleTxt.text = text
//            binding.pbLayout.root.show()
        } else {
            progressDialog?.hide()
            //binding.pbLayout.root.hide()
        }
    }

    private fun showScannerScreen(flag: Boolean) {
//        if (flag) {
//            binding.scannerView.show()
//        } else {
//            binding.scannerView.hide()
//        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner?.startPreview()
    }

    override fun onPause() {
        super.onPause()
        codeScanner?.stopPreview()
    }

    private fun startCamera() {
        if (activity == null || !isAdded) {
            return
        }
        codeScanner = CodeScanner(activity!!, binding.scannerView)
        codeScanner?.decodeCallback = DecodeCallback {
            createLogStatement("SCAN_QR_LIST", "${it.text} and ${it.barcodeFormat.name}")
            activity?.runOnUiThread {
                sendData(first = it.text)
            }
        }

        codeScanner?.startPreview()
        /*
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

                }, ContextCompat.getMainExecutor(activity?.applicationContext!!))*/


    }

    private fun sendData(first: String) {
        activity?.msg("Qr Code Detected")
        val type = WhereToGoFromScan.TESTINGCONNECTION.name
        if (args.item == Url_Text && args.type != type) {
            viewModel.checkForItemItem(first, args.type)
//            first.rawValue?.let {
//
//            } ?: activity?.msg("Oops SomeThing Went Wrong")
        } else {
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                if (showDialog) {
                    showDialog = false
                    val barcodeValue =
                        TestingBarcodeConnection(title = first, uri = first)
                    val action =
                        ScanQrCodeFragmentDirections.actionScanQrCodeFragmentToTestingConnectionFragment(
                            barcodeValue
                        )
                    findNavController().safeNavigate(action)
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
                            args.confirmreq,
                            args.selectioncls!!
                        )
                    }

                    WhereToGoFromScan.COSTESTIMATE -> {
                        ScanQrCodeFragmentDirections.actionScanQrCodeFragmentToCostDashBoardFragment(
                            FoodItemList(arr),
                            args.confirmreq,
                            args.selectioncls!!
                        )
                    }

                    WhereToGoFromScan.SHOWROOMESTIMATE -> {
                        ScanQrCodeFragmentDirections.actionScanQrCodeFragmentToShowRoomEstimationFragment(
                            FoodItemList(arr),
                            args.confirmreq,
                            args.selectioncls!!
                        )
                    }

                    WhereToGoFromScan.RESTAURANTESTIMATE -> {
                        ScanQrCodeFragmentDirections.actionScanQrCodeFragmentToRestaurantEstimationFragments(
                            FoodItemList(arr),
                            args.confirmreq,
                            args.selectioncls!!
                        )
                    }

                    WhereToGoFromScan.BILLPAYMENT -> {
                        ScanQrCodeFragmentDirections.actionScanQrCodeFragmentToBillingFragment(
                            FoodItemList(arr),
                            args.confirmreq,
                            args.selectioncls!!
                        )
                    }

                    WhereToGoFromScan.SHOWROOMBILLING -> {
                        ScanQrCodeFragmentDirections.actionScanQrCodeFragmentToShowRoomBillingFragment(
                            FoodItemList(arr),
                            args.confirmreq,
                            args.selectioncls!!
                        )
                    }

                    WhereToGoFromScan.RESTARURANTBILLING -> {
                        ScanQrCodeFragmentDirections.actionScanQrCodeFragmentToRestaurantBillingFragment(
                            FoodItemList(arr),
                            args.confirmreq,
                            args.selectioncls!!
                        )
                    }
                }

                action?.let { findNavController().safeNavigate(it) } ?: run {
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