#import "FlutterQcloudcosxmlPlugin.h"
#if __has_include(<flutter_qcloudcosxml/flutter_qcloudcosxml-Swift.h>)
#import <flutter_qcloudcosxml/flutter_qcloudcosxml-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutter_qcloudcosxml-Swift.h"
#endif

@implementation FlutterQcloudcosxmlPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterQcloudcosxmlPlugin registerWithRegistrar:registrar];
}
@end
