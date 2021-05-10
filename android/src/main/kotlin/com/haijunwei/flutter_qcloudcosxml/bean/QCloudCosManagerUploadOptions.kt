package com.haijunwei.flutter_qcloudcosxml.bean

import java.util.HashMap

/**
 *
 * @CreateDate:     2021/5/10 11:15
 * @Description:
 * @Author:         LOPER7
 * @Email:          loper7@163.com
 */
data class QCloudCosManagerUploadOptions(
        var filePath:String?="",
        var savePath:String?="",
        var bucket:String?=""
){
    fun toMap(): Map<String, Any?> {
        val toMapResult: MutableMap<String, Any?> = HashMap()
        toMapResult["filePath"] = filePath
        toMapResult["savePath"] = savePath
        toMapResult["bucket"] = bucket
        return toMapResult
    }

    companion object {
        fun fromMap(map: Map<String?, Any?>?): QCloudCosManagerUploadOptions {
            val fromMapResult = QCloudCosManagerUploadOptions()
            if (map.isNullOrEmpty()) return fromMapResult
            fromMapResult.filePath = map["filePath"] as? String
            fromMapResult.savePath = map["savePath"] as? String
            fromMapResult.bucket = map["bucket"] as?  String

            return fromMapResult
        }
    }
}