package com.kemendikbud.paud;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by akbar on 12/08/17.
 */

public class App extends Application {

    private static App instance;
    private Gson gson;
    private EventBus eventBus;
    private static Context context;

    public App() {
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        createGson();
        createEventBus();
    }

    private void createEventBus() {
        eventBus = EventBus.builder()
                .logNoSubscriberMessages(false)
                .sendNoSubscriberEvent(false)
                .build();
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    private void createGson() {
        gson = new GsonBuilder().create();
    }

    public Gson getGson() {
        return gson;
    }

    public static Context getContext(){
        return context;
    }
}
