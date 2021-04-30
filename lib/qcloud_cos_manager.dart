import 'dart:convert';

import 'package:flutter/services.dart';

class QCloudCosInitOptions {
  QCloudCosInitOptions({
    required this.region,
    this.secretId,
    this.secretKey,
    this.appId,
  });

  final String region;
  final String? secretId;
  final String? secretKey;
  final String? appId;

  Map<String, dynamic> toMap() {
    return {
      'region': region,
      'secretId': secretId,
      'secretKey': secretKey,
      'appId': appId,
    };
  }

  factory QCloudCosInitOptions.fromMap(Map<String, dynamic> map) {
    return QCloudCosInitOptions(
      region: map['region'],
      secretId: map['secretId'],
      secretKey: map['secretKey'],
      appId: map['appId'],
    );
  }

  String toJson() => json.encode(toMap());

  factory QCloudCosInitOptions.fromJson(String source) =>
      QCloudCosInitOptions.fromMap(json.decode(source));

  @override
  String toString() {
    return 'QCloudCosInitOptions(region: $region, secretId: $secretId, secretKey: $secretKey, appId: $appId)';
  }
}

class QCloudCosManagerUploadOptions {
  QCloudCosManagerUploadOptions({
    required this.filePath,
    required this.savePath,
    required this.bucket,
  });

  final String filePath;
  final String savePath;
  final String bucket;

  Map<String, dynamic> toMap() {
    return {
      'filePath': filePath,
      'savePath': savePath,
      'bucket': bucket,
    };
  }

  factory QCloudCosManagerUploadOptions.fromMap(Map<String, dynamic> map) {
    return QCloudCosManagerUploadOptions(
      filePath: map['filePath'],
      savePath: map['savePath'],
      bucket: map['bucket'],
    );
  }

  String toJson() => json.encode(toMap());

  factory QCloudCosManagerUploadOptions.fromJson(String source) =>
      QCloudCosManagerUploadOptions.fromMap(json.decode(source));

  @override
  String toString() =>
      'QCloudCosManagerUploadOptions(filePath: $filePath, savePath: $savePath, bucket: $bucket)';
}

class QCloudCosManagerUploadResult {
  QCloudCosManagerUploadResult({
    required this.key,
    required this.location,
  });

  final String key;
  final String location;

  Map<String, dynamic> toMap() {
    return {
      'key': key,
      'location': location,
    };
  }

  factory QCloudCosManagerUploadResult.fromMap(Map<String, dynamic> map) {
    return QCloudCosManagerUploadResult(
      key: map['key'],
      location: map['location'],
    );
  }

  String toJson() => json.encode(toMap());

  factory QCloudCosManagerUploadResult.fromJson(String source) =>
      QCloudCosManagerUploadResult.fromMap(json.decode(source));

  @override
  String toString() =>
      'QCloudCosManagerUploadResult(key: $key, location: $location)';
}

class QCloudCosManager {
  QCloudCosManager({BinaryMessenger? binaryMessenger})
      : _binaryMessenger = binaryMessenger;

  final BinaryMessenger? _binaryMessenger;

  Future<void> initialize(QCloudCosInitOptions arg) async {
    final Object encoded = arg.toMap();
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.QCloudCosManager.initialize',
        const StandardMessageCodec(),
        binaryMessenger: _binaryMessenger);
    final Map<Object?, Object?>? replyMap =
        await channel.send(encoded) as Map<Object?, Object?>?;
    if (replyMap == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
        details: null,
      );
    } else if (replyMap['error'] != null) {
      final Map<Object?, Object?> error =
          (replyMap['error'] as Map<Object?, Object?>?)!;
      throw PlatformException(
        code: (error['code'] as String?)!,
        message: error['message'] as String?,
        details: error['details'],
      );
    }
  }

  Future<QCloudCosManagerUploadResult> upload(
      QCloudCosManagerUploadOptions arg) async {
    final Object encoded = arg.toMap();
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.QCloudCosManager.upload',
        const StandardMessageCodec(),
        binaryMessenger: _binaryMessenger);
    final Map<Object?, Object?>? replyMap =
        await channel.send(encoded) as Map<Object?, Object?>?;
    if (replyMap == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
        details: null,
      );
    } else if (replyMap['error'] != null) {
      final Map<Object?, Object?> error =
          (replyMap['error'] as Map<Object?, Object?>?)!;
      throw PlatformException(
        code: (error['code'] as String?)!,
        message: error['message'] as String?,
        details: error['details'],
      );
    } else {
      return QCloudCosManagerUploadResult.fromMap(
          replyMap['result'] as Map<dynamic, dynamic>);
    }
  }
}
