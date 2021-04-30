//
//  Messages.swift
//  flutter_qcloudcosxml
//
//  Created by Haijun on 2021/4/30.
//

import Flutter
import Foundation

struct QCloudCosInitOptions {
    var region: String
    var secretId: String?
    var secretKey: String?
    var appId: String?
}

extension QCloudCosInitOptions {
    init(map: [String: Any]) {
        self.init(
            region: map["region"] as? String ?? "",
            secretId: map["secretId"] as? String,
            secretKey: map["secretKey"] as? String,
            appId: map["appId"] as? String
        )
    }

    func toMap() -> [String: Any] {
        return [
            "region": region,
            "secretId": secretId ?? NSNull(),
            "secretKey": secretKey ?? NSNull(),
            "appId": appId ?? NSNull(),
        ]
    }
}

struct QCloudCosManagerUploadOptions {
    var filePath: String
    var savePath: String
    var bucket: String
}

extension QCloudCosManagerUploadOptions {
    init(map: [String: Any]) {
        self.init(
            filePath: map["filePath"] as? String ?? "",
            savePath: map["savePath"] as? String ?? "",
            bucket: map["bucket"] as? String ?? ""
        )
    }

    func toMap() -> [String: Any] {
        return [
            "filePath": filePath,
            "savePath": savePath,
            "bucket": bucket,
        ]
    }
}

struct QCloudCosManagerUploadResult {
    var key: String
    var location: String
}

extension QCloudCosManagerUploadResult {
    init(map: [String: Any]) {
        self.init(
            key: map["key"] as? String ?? "",
            location: map["location"] as? String ?? ""
        )
    }

    func toMap() -> [String: Any] {
        return [
            "key": key,
            "location": location,
        ]
    }
}

protocol QCloudCosManager {
    func initialize(input: QCloudCosInitOptions, completion: @escaping (_ error: FlutterError?) -> Void)
    func upload(input: QCloudCosManagerUploadOptions, completion: @escaping (_ result: QCloudCosManagerUploadResult?, _ error: FlutterError?) -> Void)
}

func FlutterQCloudCOSXMLSetup(binaryMessenger: FlutterBinaryMessenger, api: QCloudCosManager) {
    let channel = FlutterBasicMessageChannel(name: "dev.flutter.pigeon.QCloudCosManager.initialize", binaryMessenger: binaryMessenger)
    channel.setMessageHandler { message, callback in
        let input = QCloudCosInitOptions(map: message as! [String : Any])
        api.initialize(input: input) { error in
            callback(wrapResult(result: nil, error: error))
        }
    }
    
    let channel1 = FlutterBasicMessageChannel(name: "dev.flutter.pigeon.QCloudCosManager.upload", binaryMessenger: binaryMessenger)
    channel1.setMessageHandler { message, callback in
        let input = QCloudCosManagerUploadOptions(map: message as! [String : Any])
        api.upload(input: input) { result, error in
            callback(wrapResult(result: result?.toMap(), error: error))
        }
    }
}
