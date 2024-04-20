import 'package:flutter_test/flutter_test.dart';
import 'package:cbcplugin/cbcplugin.dart';
import 'package:cbcplugin/cbcplugin_platform_interface.dart';
import 'package:cbcplugin/cbcplugin_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockCbcpluginPlatform
    with MockPlatformInterfaceMixin
    implements CbcpluginPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');

  @override
  Future<String?> decodeStringWithCBC(String value) {
    // TODO: implement decodeStringWithCBC
    throw UnimplementedError();
  }

  @override
  Future<String?> encodeStringWithCBC(String value) {
    // TODO: implement encodeStringWithCBC
    throw UnimplementedError();
  }
}

void main() {
  final CbcpluginPlatform initialPlatform = CbcpluginPlatform.instance;

  test('$MethodChannelCbcplugin is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelCbcplugin>());
  });

  // test('getPlatformVersion', () async {
  //   Cbcplugin cbcpluginPlugin = Cbcplugin();
  //   MockCbcpluginPlatform fakePlatform = MockCbcpluginPlatform();
  //   CbcpluginPlatform.instance = fakePlatform;
  //
  //   expect(await cbcpluginPlugin.getPlatformVersion(), '42');
  // });
}
