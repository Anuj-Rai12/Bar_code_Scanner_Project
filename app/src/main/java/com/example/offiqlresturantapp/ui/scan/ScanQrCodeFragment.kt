package com.example.offiqlresturantapp.ui.scan

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.offiqlresturantapp.MainActivity
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.data.item_master_sync.json.ItemMaster
import com.example.offiqlresturantapp.data.login.model.TestingBarcodeConnection
import com.example.offiqlresturantapp.databinding.ScanQrLayoutBinding
import com.example.offiqlresturantapp.ui.scan.utils.LuminosityAnalyzer
import com.example.offiqlresturantapp.ui.searchfood.model.FoodItemList
import com.example.offiqlresturantapp.ui.searchfood.model.ItemMasterFoodItem
import com.example.offiqlresturantapp.utils.*
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@AndroidEntryPoint
class ScanQrCodeFragment : Fragment(R.layout.scan_qr_layout) {
    private lateinit var binding: ScanQrLayoutBinding
    private var imageCapture: ImageCapture? = null
    private var flagList: Boolean = false
    private lateinit var cameraExecutor: ExecutorService
    private val args: ScanQrCodeFragmentArgs by navArgs()

    private val option = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_AZTEC,
            Barcode.FORMAT_CODE_93,
            Barcode.FORMAT_CODABAR,
            Barcode.FORMAT_CODE_128,
            Barcode.FORMAT_CODE_39,
            Barcode.FORMAT_EAN_8,
            Barcode.FORMAT_EAN_13,
            Barcode.FORMAT_PDF417,
            Barcode.FORMAT_UPC_A,
            Barcode.FORMAT_UPC_E,
        )
        .build()


    private val scanner by lazy {
        BarcodeScanning.getClient(option)
        //  BarcodeScanning.getClient()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().changeStatusBarColor(R.color.black)
        binding = ScanQrLayoutBinding.bind(view)
        cameraExecutor = Executors.newSingleThreadExecutor()
        (activity as MainActivity?)?.getPermission()
        startCamera()
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
                                flagList = false
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
        if (args.tbl != null) {
            try {
                gotConfirmScreen(barcode = first)
            }catch (e:Exception){
                activity?.msg("Try Some Other QR")
            }
        } else {
            val barcodeValue =
                TestingBarcodeConnection(title = first.url?.title, uri = first.url?.url)
            val action =
                ScanQrCodeFragmentDirections.actionScanQrCodeFragmentToTestingConnectionFragment(
                    barcodeValue
                )
            findNavController().navigate(action)
        }
    }

    private fun gotConfirmScreen(barcode: Barcode) {
        deserializeFromJson<ItemMaster>(barcode.rawValue)?.also { value ->
            showQtyDialog("Select Quantity", itemMaster = value, cancel = {
                flagList = it
            }, res = { res ->
                val arr = ArrayList<ItemMasterFoodItem>()
                args.food?.let { item ->
                    arr.addAll(item.foodList)
                }
                Log.i("QR", "sendData: $res")
                arr.add(ItemMasterFoodItem(res, res.foodQty, res.foodAmt))
                lifecycleScope.launchWhenResumed {
                    val action =
                        ScanQrCodeFragmentDirections.actionScanQrCodeFragmentToConfirmOderFragment(
                            FoodItemList(arr),
                            args.tbl!!
                        )
                    findNavController().navigate(action)
                }
            })
        } ?: activity?.msg("Oops Something Went Wrong?")
    }


}