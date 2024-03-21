package skm.notification_listener.skm_notification_listener;

import static skm.notification_listener.skm_notification_listener.NotificationConstants.*;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import io.flutter.plugin.common.EventChannel.EventSink;

import java.util.HashMap;

public class NotificationReceiver extends BroadcastReceiver {

    private final EventSink eventSink;

    public NotificationReceiver(EventSink eventSink) {
        this.eventSink = eventSink;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String packageName = intent.getStringExtra(PACKAGE_NAME);
        String title = intent.getStringExtra(NOTIFICATION_TITLE);
        String content = intent.getStringExtra(NOTIFICATION_CONTENT);
        byte[] notificationIcon = intent.getByteArrayExtra(NOTIFICATIONS_ICON);
        byte[] notificationExtrasPicture = intent.getByteArrayExtra(EXTRAS_PICTURE);
        byte[] largeIcon = intent.getByteArrayExtra(NOTIFICATIONS_LARGE_ICON);
        boolean haveExtraPicture = intent.getBooleanExtra(HAVE_EXTRA_PICTURE, false);
        boolean hasRemoved = intent.getBooleanExtra(IS_REMOVED, false);
        boolean canReply = intent.getBooleanExtra(CAN_REPLY, false);
        int id = intent.getIntExtra(ID, -1);


        HashMap<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("packageName", packageName);
        data.put("title", title);
        data.put("content", content);
        data.put("notificationIcon", notificationIcon);
        data.put("notificationExtrasPicture", notificationExtrasPicture);
        data.put("haveExtraPicture", haveExtraPicture);
        data.put("largeIcon", largeIcon);
        data.put("hasRemoved", hasRemoved);
        data.put("canReply", canReply);

        eventSink.success(data);
    }
}