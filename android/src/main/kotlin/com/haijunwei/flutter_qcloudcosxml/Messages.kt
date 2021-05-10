package com.haijunwei.flutter_qcloudcosxml

import com.haijunwei.flutter_qcloudcosxml.bean.QCloudCosInitOptions
import com.haijunwei.flutter_qcloudcosxml.bean.QCloudCosManagerUploadOptions
import com.haijunwei.flutter_qcloudcosxml.bean.QCloudCosManagerUploadResult
import io.flutter.plugin.common.BasicMessageChannel
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.StandardMessageCodec
import java.util.*

/** Generated class from Pigeon.  */
object Messages {
    /** Sets up an instance of `PhotoPicker` to handle messages through the `binaryMessenger`.  */
    fun setup(binaryMessenger: BinaryMessenger?, api: QCloudCosXml?) {
        run {
            initialize(binaryMessenger,api)
            upload(binaryMessenger,api)
        }
    }

    private fun initialize(binaryMessenger: BinaryMessenger?, api: QCloudCosXml?){
        val channel = BasicMessageChannel(binaryMessenger!!, "dev.flutter.pigeon.QCloudCosManager.initialize", StandardMessageCodec())
        if (api == null) {
            channel.setMessageHandler(null)
            return
        }
        channel.setMessageHandler { message: Any?, reply: BasicMessageChannel.Reply<Any> ->
            val wrapped: MutableMap<String, Any> = HashMap()
            try {
                val input = QCloudCosInitOptions.fromMap(message as Map<String?, Any?>?)
                api.initialize(input, object : Result<Any?> {
                    override fun success(result: Any?) {
                        val map:MutableMap<String, Any?> = HashMap()
                        map["message"] = "success"
                        wrapped["result"] = map
                        reply.reply(wrapped)
                    }

                    override fun error(throwable: Throwable) {
                        wrapped["error"] = wrapError(throwable)
                        reply.reply(wrapped)
                    }
                })
            } catch (exception: Error) {
                wrapped["error"] = wrapError(exception)
                reply.reply(wrapped)
            } catch (exception: RuntimeException) {
                wrapped["error"] = wrapError(exception)
                reply.reply(wrapped)
            }
        }
    }

    private fun upload(binaryMessenger: BinaryMessenger?, api: QCloudCosXml?){
        val channel = BasicMessageChannel(binaryMessenger!!, "dev.flutter.pigeon.QCloudCosManager.upload", StandardMessageCodec())
        if (api == null) {
            channel.setMessageHandler(null)
            return
        }

        channel.setMessageHandler { message: Any?, reply: BasicMessageChannel.Reply<Any> ->
            val wrapped: MutableMap<String, Any> = HashMap()
            try {
                val input = QCloudCosManagerUploadOptions.fromMap(message as Map<String?, Any?>?)
                api.upload(input, object : Result<QCloudCosManagerUploadResult?> {
                    override fun success(result: QCloudCosManagerUploadResult?) {
                        wrapped["result"] = result?.toMap()?:""
                        reply.reply(wrapped)
                    }

                    override fun error(throwable: Throwable) {
                        wrapped["error"] = wrapError(throwable)
                        reply.reply(wrapped)
                    }
                })
            } catch (exception: Error) {
                wrapped["error"] = wrapError(exception)
                reply.reply(wrapped)
            } catch (exception: RuntimeException) {
                wrapped["error"] = wrapError(exception)
                reply.reply(wrapped)
            }
        }
    }
}

private fun wrapError(exception: Throwable): Map<String, Any?> {
    val errorMap: MutableMap<String, Any?> = HashMap()
    errorMap["message"] = exception.toString()
    errorMap["code"] = exception.javaClass.simpleName
    errorMap["details"] = null
    return errorMap
}


interface Result<T> {
    fun success(result: T)

    fun error(throwable:Throwable)
}

/** Generated interface from Pigeon that represents a handler of messages from Flutter. */
interface QCloudCosXml {
    fun upload(options: QCloudCosManagerUploadOptions?, result: Result<QCloudCosManagerUploadResult?>?)
    fun initialize(options: QCloudCosInitOptions?, result: Result<Any?>?)
}