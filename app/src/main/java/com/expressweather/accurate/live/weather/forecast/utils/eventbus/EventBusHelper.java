package com.expressweather.accurate.live.weather.forecast.utils.eventbus;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

public class EventBusHelper {
        private static EventBus eventBus = EventBus.getDefault();

        public static boolean checkExist(Context context) {
            return eventBus.isRegistered(context);
        }

        public static void register(Object subscriber) {
            eventBus.register(subscriber);
        }

        public static void unregister(Object subscriber) {
            eventBus.unregister(subscriber);
        }

        public static void removeSticky(Object event) {
            eventBus.removeStickyEvent(event);
        }

        public static void post(Object event) {
            eventBus.post(event);
        }

        public static void postSticky(Object event) {
            eventBus.postSticky(event);
        }

}
