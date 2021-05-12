package com.haijunwei.flutter_qcloudcosxml

import android.util.Log
import androidx.annotation.NonNull
import com.haijunwei.flutter_qcloudcosxml.bean.QCloudCosInitOptions
import com.haijunwei.flutter_qcloudcosxml.bean.QCloudCosManagerUploadOptions
import com.haijunwei.flutter_qcloudcosxml.bean.QCloudCosManagerUploadResult
import com.tencent.cos.xml.CosXmlService
import com.tencent.cos.xml.exception.CosXmlClientException
import com.tencent.cos.xml.exception.CosXmlServiceException
import com.tencent.cos.xml.ktx.cosService
import com.tencent.cos.xml.listener.CosXmlResultListener
import com.tencent.cos.xml.model.CosXmlRequest
import com.tencent.cos.xml.model.CosXmlResult
import com.tencent.cos.xml.transfer.COSXMLUploadTask.COSXMLUploadTaskResult
import com.tencent.cos.xml.transfer.TransferConfig
import com.tencent.cos.xml.transfer.TransferManager
import com.tencent.qcloud.core.auth.QCloudCredentialProvider
import com.tencent.qcloud.core.auth.SessionQCloudCredentials
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import java.lang.Exception


/** FlutterQcloudcosxmlPlugin */
class FlutterQcloudcosxmlPlugin : FlutterPlugin, MethodCallHandler, ActivityAware, QCloudCosXml {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private var activityBinding: ActivityPluginBinding? = null
    private var cosXmlService: CosXmlService? = null

    companion object {
        fun registerWith(registrar: Registrar) {
            if (registrar.activity() == null) {
                // If a background flutter view tries to register the plugin, there will be no activity from the registrar,
                // we stop the registering process immediately because the ImagePicker requires an activity.
                return
            }
            val plugin = FlutterQcloudcosxmlPlugin()
            Messages.setup(registrar.messenger(), plugin)
        }
    }

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_qcloudcosxml")
        channel.setMethodCallHandler(this)
        Messages.setup(flutterPluginBinding.binaryMessenger, this)
    }


    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        } else {
            result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onDetachedFromActivity() {
        activityBinding = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        onAttachedToActivity(binding)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activityBinding = binding
    }

    override fun onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity()
    }

    override fun initialize(options: QCloudCosInitOptions?, result: com.haijunwei.flutter_qcloudcosxml.Result<Any?>?) {
        if (options == null)
            return
        cosXmlService = activityBinding?.activity?.let {
            cosService(context = it) {
                configuration {
                    setRegion(options.region)
                    isHttps(true)
                }
                credentialProvider {
                    if (options.sessionToken.isNullOrEmpty())
                        ShortTimeCredentialProvider(options.secretId, options.secretKey, 300)
                    else
                        lifecycleCredentialProvider {
                            return@lifecycleCredentialProvider SessionQCloudCredentials(
                                    options.secretId,
                                    options.secretKey,
                                    options.sessionToken,
                                    1556183496
                            )

                        }
                }
            }
        }
        result?.success(null)
    }

    override fun upload(options: QCloudCosManagerUploadOptions?, result: com.haijunwei.flutter_qcloudcosxml.Result<QCloudCosManagerUploadResult?>?) {
        if (cosXmlService == null){
            val throwable = Throwable("Must call initialize() first to initialize")
            result?.error(throwable)
            return
        }

        options?.apply {
            Log.d("QcloudcosxmlPlugin",options.toMap().toString())
            TransferManager(cosXmlService, TransferConfig.Builder().build())
                    .upload(bucket, savePath, filePath, null)
                    .let {
                        it.setCosXmlResultListener(object : CosXmlResultListener {
                            override fun onSuccess(cosXmlRequest: CosXmlRequest?, cosXmlResult: CosXmlResult?) {
                                try {
                                    val cOSXMLUploadTaskResult = cosXmlResult as COSXMLUploadTaskResult
                                    val data = QCloudCosManagerUploadResult()
                                    data.key = cOSXMLUploadTaskResult.picUploadResult.originalInfo.key
                                    data.location = cOSXMLUploadTaskResult.picUploadResult.originalInfo.location
                                    result?.success(data)
                                }catch (e:Exception){
                                    Log.d("QcloudcosxmlPlugin","upload error :${e.message}")
                                    result?.error(e)
                                }



                            }

                            override fun onFail(cosXmlRequest: CosXmlRequest?, exception: CosXmlClientException?, serviceException: CosXmlServiceException?) {
                                Log.d("QcloudcosxmlPlugin","upload error :${exception?.message}")
                                val throwable = Throwable(exception?.message
                                        ?: serviceException?.message)
                                result?.error(throwable)
                            }
                        })

                        it.setCosXmlProgressListener { complete, target ->
                            Log.d("QcloudcosxmlPlugin","uploading:${complete}/${target}")
                        }


                        it.setTransferStateListener {
                            Log.d("QcloudcosxmlPlugin","upload state :${it.name}")
                        }
                    }
        }


    }


}
