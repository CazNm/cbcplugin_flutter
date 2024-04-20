import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'cbcplugin_platform_interface.dart';

/// An implementation of [CbcpluginPlatform] that uses method channels.
class MethodChannelCbcplugin extends CbcpluginPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('cbcplugin');


  @override
  Future<String?> encodeStringWithCBC(String value) async {
    final version = await methodChannel.invokeMethod<String>('encodeStringWithCBC', {
      "value" : value
    });
    return version;
  }

  @override
  Future<String?> decodeStringWithCBC(String value) async{
    final version = await methodChannel.invokeMethod<String>('decodeStringWithCBC', {
      "value" : value
    });
    return version;
  }
}
