import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'cbcplugin_method_channel.dart';

abstract class CbcpluginPlatform extends PlatformInterface {
  /// Constructs a CbcpluginPlatform.
  CbcpluginPlatform() : super(token: _token);

  static final Object _token = Object();

  static CbcpluginPlatform _instance = MethodChannelCbcplugin();

  /// The default instance of [CbcpluginPlatform] to use.
  ///
  /// Defaults to [MethodChannelCbcplugin].
  static CbcpluginPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [CbcpluginPlatform] when
  /// they register themselves.
  static set instance(CbcpluginPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> encodeStringWithCBC(String value) {
    throw UnimplementedError('encodeStringWithCBC() has not been implemented.');
  }

  Future<String?> decodeStringWithCBC(String value) {
    throw UnimplementedError('decodeStringWithCBC() has not been implemented.');
  }
}
