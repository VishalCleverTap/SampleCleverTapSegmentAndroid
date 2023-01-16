package com.example.sampleclevertapsegmentandroid;

import android.app.Application;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import com.clevertap.android.sdk.ActivityLifecycleCallback;
import com.clevertap.android.sdk.CleverTapAPI;
import com.segment.analytics.Analytics;
import com.segment.analytics.android.integrations.clevertap.CleverTapIntegration;

import java.util.HashMap;

public class CleverTapSegmentApplication extends Application  {

    private static final String TAG = String.format("%s.%s", "CLEVERTAP", CleverTapSegmentApplication.class.getName());
    private static final String CLEVERTAP_KEY = "CleverTap";
    public static boolean sCleverTapSegmentEnabled = false;

    private CleverTapAPI cleverTapInstance;
    private Analytics analytics;


    @Override public void onCreate() {


        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.DEBUG);

        ActivityLifecycleCallback.register(this);
        super.onCreate();

        String WRITE_KEY = getResources().getString(R.string.segment_write_key);

        analytics = new Analytics.Builder(getApplicationContext(), WRITE_KEY)
                .logLevel(Analytics.LogLevel.VERBOSE)
                .use(CleverTapIntegration.FACTORY)
                .build();

        analytics.onIntegrationReady(CLEVERTAP_KEY, new Analytics.Callback<CleverTapAPI>() {
            @Override
            public void onReady(CleverTapAPI instance) {
                Log.i(TAG, "analytics.onIntegrationReady() called");
                CleverTapIntegrationReady(instance);
            }
        });

        Analytics.setSingletonInstance(analytics);
    }

    private void CleverTapIntegrationReady(CleverTapAPI instance) {
        instance.enablePersonalization();
        sCleverTapSegmentEnabled = true;
        cleverTapInstance = instance;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CleverTapAPI.createNotificationChannel(getApplicationContext(), "Promotion", "Promotion Channel",
                    "For Promotion PN",
                    NotificationManager.IMPORTANCE_MAX, true);
        }
    }

    public CleverTapAPI getCleverTapInstance() {
        return cleverTapInstance;
    }

    public Analytics getAnalytics() {
        return analytics;
    }


}
