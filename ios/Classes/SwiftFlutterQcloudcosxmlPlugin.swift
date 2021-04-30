import Flutter
import UIKit

public class SwiftFlutterQcloudcosxmlPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "flutter_qcloudcosxml", binaryMessenger: registrar.messenger())
    let instance = SwiftFlutterQcloudcosxmlPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    result("iOS " + UIDevice.current.systemVersion)
  }
}
