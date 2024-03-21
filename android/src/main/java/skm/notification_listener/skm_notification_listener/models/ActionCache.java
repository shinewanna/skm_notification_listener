package skm.notification_listener.skm_notification_listener.models;

import java.util.HashMap;

abstract public class ActionCache {
    public static HashMap<Integer, Action> cachedNotifications = new HashMap<>();
}