package com.codip.seed.cbcplugin

import android.util.Log
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.util.Base64

/** CbcpluginPlugin */
class CbcpluginPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "cbcplugin")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    when(call.method) {
      ENCODE_STRING_WITH_CBC -> {
        val encodeResult = call.argument<String>("value")?.let { it ->
          encodeString(it)
        }
        result.success(encodeResult)
      }
      DECODE_STRING_WITH_CBC -> {
        val decodeResult = call.argument<String>("value")?.let { it ->
          decodeString(it)
        }
        result.success(decodeResult)
      } else -> {
        result.notImplemented()
      }
    }
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  private fun encodeString(domainString: String): String {

    var plainText = domainString.toByteArray(Charsets.UTF_8)

    while (plainText.size < 16) {
      plainText = plainText.plus(0)
    }

    val cipherText = ByteArray(144)

    seedCBC.init(SEEDCBC.ENC, keyIvByteArray, keyIvByteArray)

    var outPutTextLength = seedCBC.process(
      plainText,
      0,
      plainText.size,
      cipherText,
      0
    )
    outPutTextLength = seedCBC.close(cipherText, outPutTextLength)

    val encodeText = Base64.getEncoder().encode(cipherText.copyOfRange(0, outPutTextLength))

    return String(encodeText, charset("UTF-8"))
  }

  private fun decodeString(encodedString: String): String {

    var plainText = Base64.getDecoder().decode(encodedString)

    while (plainText.size < 16) {
      plainText = plainText.plus(0)
    }

    val cipherText = ByteArray(144)

    seedCBC.init(SEEDCBC.DEC, keyIvByteArray, keyIvByteArray)

    var outPutTextLength = seedCBC.process(
      plainText,
      0,
      plainText.size,
      cipherText,
      0
    )

    outPutTextLength = seedCBC.close(cipherText, outPutTextLength)

    return String(cipherText.copyOfRange(0, outPutTextLength), Charsets.UTF_8)
  }

  companion object {
    const val keyIvList = "1234567890ABCDEF"
    val keyIvByteArray: ByteArray = keyIvList.toByteArray(Charsets.UTF_8)
    val seedCBC: SEEDCBC = SEEDCBC()

    const val ENCODE_STRING_WITH_CBC = "encodeStringWithCBC"
    const val DECODE_STRING_WITH_CBC = "decodeStringWithCBC"
  }
}
