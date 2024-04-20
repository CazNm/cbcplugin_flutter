
import 'cbcplugin_platform_interface.dart';

class Cbcplugin {
  Future<String?> encodeStringWithCBC(String value) {
    return CbcpluginPlatform.instance.encodeStringWithCBC(value);
  }

  Future<String?> decodeStringWithCBC(String value) {
    return CbcpluginPlatform.instance.decodeStringWithCBC(value);
  }
}
