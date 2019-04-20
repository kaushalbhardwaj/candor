package com.indusbit.candorsdk;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

class CandorActivityLifecycleCallbacks  implements Application.ActivityLifecycleCallbacks {
    private static String TAG = "Lifecycle_Callbacks";
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.d(TAG,"activity created");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.d(TAG,"activity started" + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.d(TAG,"activity resumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.d(TAG,"activity paused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.d(TAG,"activity stopped");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}

