# skm_notification_listener

A flutter plugin for interacting with Notification Service in Android.

NotificationListenerService is a service that receives calls from the system when new notifications are posted or removed,

for more info check [NotificationListenerService](https://developer.android.com/reference/android/service/notification/NotificationListenerService)

### Installation and usage

Add package to your pubspec:

```yaml
dependencies:
  skm_notification_listener: any # or the latest version on Pub
```

Inside AndroidManifest add this to bind notification service with your application

```xml
<service
    android:label="notifications"
    android:name="skm.notification_listener.skm_notification_listener.NotificationListener"
    android:permission="android.permission.BIND_NOTIFICATION_LISTENER_SERVICE"
    android:exported="true">
    <intent-filter>
        <action android:name="android.service.notification.NotificationListenerService" />
    </intent-filter>
</service>
```

### USAGE

```dart
 /// check if notification permission is enabled
 final bool status = await NotificationListenerService.isPermissionGranted();

 /// request notification permission
 /// it will open the notifications settings page and return `true` once the permission granted.
 final bool status = await NotificationListenerService.requestPermission();

 /// stream the incoming notification events
  NotificationListenerService.notificationsStream.listen((event) {
    log("Current notification: $event");
  });
```

The `ServiceNotificationEvent` provides:

```dart
  /// the notification id
  int? id;

  /// check if we can reply the Notification
  bool? canReply;

  /// if the notification has an extras image
  bool? haveExtraPicture;

  /// if the notification has been removed
  bool? hasRemoved;

  /// notification extras image
  /// To display an image simply use the [Image.memory] widget.
  Uint8List? extrasPicture;

  /// notification large icon
  /// To display an image simply use the [Image.memory] widget.
  Uint8List? largeIcon;

  /// notification package name
  String? packageName;

  /// notification title
  String? title;

  /// the notification app icon
  /// To display an image simply use the [Image.memory] widget.
  Uint8List? appIcon;

  /// the content of the notification
  String? content;

  /// send a direct message reply to the incoming notification
  Future<bool> sendReply(String message)

```

To reply to a notification provides:

```dart
  try {
    await event.sendReply("This is an auto response");
  } catch (e) {
    log(e.toString());
  }

```

## Screenshots

<!-- <img src="https://user-images.githubusercontent.com/22800380/165560254-fc72ed1f-a31e-4498-b6de-18cea539ca11.png" width="300">
 -->
