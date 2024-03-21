import 'dart:developer';
import 'dart:io';

import 'package:flutter/services.dart';
import 'package:skm_notification_listener/notification_event.dart';

const MethodChannel methodChannel = MethodChannel('skm/notifications_channel');
const EventChannel _eventChannel = EventChannel('skm/notifications_event');
Stream<ServiceNotificationEvent>? _stream;

class SkmNotificationListener {
  Future<String?> getPlatformVersion() {
    return Future.value('42');
  }

  /// stream the incoming notifications events
  static Stream<ServiceNotificationEvent> get notificationsStream {
    if (Platform.isAndroid) {
      _stream ??=
          _eventChannel.receiveBroadcastStream().map<ServiceNotificationEvent>(
                (event) => ServiceNotificationEvent.fromMap(event),
              );
      return _stream!;
    }
    throw Exception("Notifications API exclusively available on Android!");
  }

  /// request notification permission
  /// it will open the notification settings page and return `true` once the permission granted.
  static Future<bool> requestPermission() async {
    try {
      return await methodChannel.invokeMethod('requestPermission');
    } on PlatformException catch (error) {
      log("$error");
      return Future.value(false);
    }
  }

  /// check if notification permission is enabled
  static Future<bool> isPermissionGranted() async {
    try {
      return await methodChannel.invokeMethod('isPermissionGranted');
    } on PlatformException catch (error) {
      log("$error");
      return false;
    }
  }
}
