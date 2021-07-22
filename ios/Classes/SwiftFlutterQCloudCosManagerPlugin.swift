import Flutter
import QCloudCOSXML
import UIKit

public class SwiftFlutterQCloudCosManagerPlugin: NSObject, FlutterPlugin, QCloudCosManager, QCloudSignatureProvider {
    private var isInitialized = false
    private var secretId: String = ""
    private var secretKey: String = ""

    public static func register(with registrar: FlutterPluginRegistrar) {
        let instance = SwiftFlutterQCloudCosManagerPlugin()
        FlutterQCloudCOSXMLSetup(binaryMessenger: registrar.messenger(), api: instance)
        registrar.publish(instance)
    }

    func initialize(input: QCloudCosInitOptions, completion: (FlutterError?) -> Void) {
        if isInitialized {
            completion(nil)
            return
        }
        secretId = input.secretId ?? ""
        secretKey = input.secretKey ?? ""
        let config = QCloudServiceConfiguration()
        let endpoint = QCloudCOSXMLEndPoint()
        endpoint.regionName = input.region
        config.appID = input.appId
        config.endpoint = endpoint
        config.signatureProvider = self
        QCloudCOSXMLService.registerDefaultCOSXML(with: config)
        QCloudCOSTransferMangerService.registerDefaultCOSTransferManger(with: config)
        completion(nil)
        isInitialized = true
    }

    func upload(input: QCloudCosManagerUploadOptions, completion: @escaping (QCloudCosManagerUploadResult?, FlutterError?) -> Void) {
        let request = QCloudCOSXMLUploadObjectRequest<AnyObject>()
        request.bucket = input.bucket
        request.body = URL(fileURLWithPath: input.filePath) as AnyObject
        request.object = input.savePath
        request.setFinish { result, error in
            if let e = error {
                completion(nil, FlutterError(code: "0", message: e.localizedDescription, details: nil))
            } else if let result = result {
                completion(QCloudCosManagerUploadResult(key: result.key, location: result.location), nil)
            }
        }
        QCloudCOSTransferMangerService.defaultCOSTransferManager().uploadObject(request)
    }

    // MARK: -

    public func signature(with fileds: QCloudSignatureFields!, request: QCloudBizHTTPRequest!, urlRequest urlRequst: NSMutableURLRequest!, compelete continueBlock: QCloudHTTPAuthentationContinueBlock!) {
        let credential = QCloudCredential()
        credential.secretID = secretId
        credential.secretKey = secretKey
        credential.startDate = Date()
        credential.expirationDate = Date(timeIntervalSinceNow: 1 * 60 * 60 * 24)
        let creator = QCloudAuthentationV5Creator(credential: credential)
        let signature = creator?.signature(forData: urlRequst)
        continueBlock(signature, nil)
    }
}

func wrapResult(result: [String: Any]?, error: FlutterError?) -> [String: Any] {
    var errorDict: [String: Any]?
    if let _error = error {
        errorDict = [
            "code": _error.code,
            "message": _error.message ?? NSNull(),
            "details": _error.details ?? NSNull(),
        ]
    }
    return [
        "result": result ?? NSNull(),
        "error": errorDict ?? NSNull(),
    ]
}
