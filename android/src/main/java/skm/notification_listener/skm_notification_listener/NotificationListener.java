package skm.notification_listener.skm_notification_listener;

import static skm.notification_listener.skm_notification_listener.NotificationUtils.getBitmapFromDrawable;
import static skm.notification_listener.skm_notification_listener.models.ActionCache.cachedNotifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import skm.notification_listener.skm_notification_listener.models.Action;


@SuppressLint("OverrideAbstract")
public class NotificationListener extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification notification) {
        handleNotification(notification, false);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        handleNotification(sbn, true);
    }

    private void handleNotification(StatusBarNotification notification, boolean isRemoved) {
        String packageName = notification.getPackageName();
        Bundle extras = notification.getNotification().extras;
//        byte[] appIcon = getAppIcon(packageName);
//        byte[] largeIcon = null;
        Action action = NotificationUtils.getQuickReplyAction(notification.getNotification(), packageName);

//        if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
//            largeIcon = getNotificationLargeIcon(getApplicationContext(), notification.getNotification());
//        }

        Intent intent = new Intent(NotificationConstants.INTENT);
        intent.putExtra(NotificationConstants.PACKAGE_NAME, packageName);
        intent.putExtra(NotificationConstants.ID, notification.getId());
        intent.putExtra(NotificationConstants.CAN_REPLY, action != null);

        if (NotificationUtils.getQuickReplyAction(notification.getNotification(), packageName) != null) {
            cachedNotifications.put(notification.getId(), action);
        }

//        intent.putExtra(NotificationConstants.NOTIFICATIONS_ICON, appIcon);
//        intent.putExtra(NotificationConstants.NOTIFICATIONS_LARGE_ICON, largeIcon);

        CharSequence title = extras.getCharSequence(Notification.EXTRA_TITLE);
        CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);

//        for (String key : extras.keySet()) {
//            // Get the value corresponding to the key
//            Object value = extras.get(key);
//
//            // Log the key-value pair
//            Log.i("Notification Extra", key + ": " + value);
//        }

        intent.putExtra(NotificationConstants.NOTIFICATION_TITLE, title == null ? null : title.toString());
        intent.putExtra(NotificationConstants.NOTIFICATION_CONTENT, text == null ? null : text.toString());
        intent.putExtra(NotificationConstants.IS_REMOVED, isRemoved);
//        intent.putExtra(NotificationConstants.HAVE_EXTRA_PICTURE, extras.containsKey(Notification.EXTRA_PICTURE));

//        if (extras.containsKey(Notification.EXTRA_PICTURE)) {
//            Bitmap bmp = (Bitmap) extras.get(Notification.EXTRA_PICTURE);
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            if(bmp != null) {
//                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            }
//            intent.putExtra(NotificationConstants.EXTRAS_PICTURE, stream.toByteArray());
//        }
        sendBroadcast(intent);
    }


    public byte[] getAppIcon(String packageName) {
        try {
            PackageManager manager = getBaseContext().getPackageManager();
            Drawable icon = manager.getApplicationIcon(packageName);

            if (icon == null) {
                return null; // Return null if icon not found
            }

            Bitmap bitmap = getBitmapFromDrawable(icon); // Assuming getBitmapFromDrawable is implemented correctly
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("getAppIconSafe", "Error getting app icon for " + packageName, e); // More informative logging
            return null;
        }
    }

    @RequiresApi(api = VERSION_CODES.M)
    private byte[] getNotificationLargeIcon(Context context, Notification notification) {
        try {
            Icon largeIcon = notification.getLargeIcon();
            if (largeIcon == null) {
                return null;
            }
            Drawable iconDrawable = largeIcon.loadDrawable(context);
            assert iconDrawable != null;
            Bitmap iconBitmap = ((BitmapDrawable) iconDrawable).getBitmap();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            iconBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            return outputStream.toByteArray();
        } catch (Exception e) {
//            e.printStackTrace();
            Log.d("ERROR LARGE ICON", "getNotificationLargeIcon: " + e.getMessage());
            return null;
        }
    }

}