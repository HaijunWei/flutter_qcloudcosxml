package com.haijunwei.flutter_qcloudcosxml.bean

import java.util.HashMap

/**
 *
 * @CreateDate:     2021/5/10 11:25
 * @Description:
 * @Author:         LOPER7
 * @Email:          loper7@163.com
 */
class QCloudCosManagerUploadResult(
        var key:String?="",
        var location:String?=""
){
    fun toMap(): Map<String, Any?> {
        val toMapResult: MutableMap<String, Any?> = HashMap()
        toMapResult["key"] = key
        toMapResult["location"] = location
        return toMapResult
    }

    companion object {
        fun fromMap(map: Map<String?, Any?>?): QCloudCosManagerUploadResult {
            val fromMapResult = QCloudCosManagerUploadResult()
            if (map.isNullOrEmpty()) return fromMapResult
            fromMapResult.key = map["key"] as? String
            fromMapResult.location = map["location"] as? String

            return fromMapResult
        }
    }
}