package com.haijunwei.flutter_qcloudcosxml.bean

import java.util.HashMap

/**
 *
 * @CreateDate:     2021/5/10 11:11
 * @Description:
 * @Author:         LOPER7
 * @Email:          loper7@163.com
 */
data class QCloudCosInitOptions(
        var region: String? = "",
        var secretId: String? = "",
        var secretKey: String? = "",
        var sessionToken: String? = "",
        var appId: String? = ""
) {
    fun toMap(): Map<String, Any?> {
        val toMapResult: MutableMap<String, Any?> = HashMap()
        toMapResult["region"] = region
        toMapResult["secretId"] = secretId
        toMapResult["secretKey"] = secretKey
        toMapResult["appId"] = appId
        return toMapResult
    }

    companion object {
        fun fromMap(map: Map<String?, Any?>?): QCloudCosInitOptions {
            val fromMapResult = QCloudCosInitOptions()
            if (map.isNullOrEmpty()) return fromMapResult
            fromMapResult.region = map["region"] as? String
            fromMapResult.secretId = map["secretId"] as? String
            fromMapResult.secretKey = map["secretKey"] as?  String
            fromMapResult.appId = map["appId"] as? String

            return fromMapResult
        }
    }
}