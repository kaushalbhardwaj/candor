package com.indusbit.candorsdk;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class CandorHelper {

    private static final String TAG = "CandorHelper";
    private static Context context;

    public static Map<String, String> getDeviceInfo(Context context) {
        final Map<String, String> deviceInfo = new HashMap<String, String>();
        deviceInfo.put("$android_lib_version", CandorConst.LIB_VERSION);
        deviceInfo.put("$android_os", "Android");
        deviceInfo.put("$android_os_version", Build.VERSION.RELEASE == null ? "UNKNOWN" : Build.VERSION.RELEASE);
        deviceInfo.put("$android_manufacturer", Build.MANUFACTURER == null ? "UNKNOWN" : Build.MANUFACTURER);
        deviceInfo.put("$android_brand", Build.BRAND == null ? "UNKNOWN" : Build.BRAND);
        deviceInfo.put("$android_model", Build.MODEL == null ? "UNKNOWN" : Build.MODEL);
        try {
            final PackageManager manager = context.getPackageManager();
            final PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            deviceInfo.put("$android_app_version", info.versionName);
            deviceInfo.put("$android_app_version_code", Integer.toString(info.versionCode));
        } catch (final PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Exception getting app version name", e);
        }
        return Collections.unmodifiableMap(deviceInfo);

    }

    public static void initialize(final Context con, String userId) {
        context = con;
        Map<String, String> deviceInfo = getDeviceInfo(context);
        registerActivityLifecycleCallbacks(context);
        String accountToken = null;

        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = info.metaData;
            accountToken = bundle.getString("io.candor.AccountToken");
            Log.d(TAG, accountToken);

        } catch (Exception exception) {

            Log.e(TAG, "please add the account token");
            return;
        }

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<Experiments> call = apiService.getExperiments(accountToken, "kaushal");
        try {

            call.enqueue(new Callback<Experiments>() {
                @Override
                public void onResponse(Call<Experiments> call, Response<Experiments> response) {

                    CandorDBHelper dbHelper = new CandorDBHelper(context);
                    dbHelper.saveExperiments(response.body().experiments);

                }

                @Override
                public void onFailure(Call<Experiments> call, Throwable t) {
                    Log.d(TAG, "error is-" + t.toString());

                }
            });

        } catch (Exception e) {
            Log.d(TAG, "exception");
        }

    }

    public static void registerActivityLifecycleCallbacks(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (context.getApplicationContext() instanceof Application) {
                final Application app = (Application) context.getApplicationContext();
                CandorActivityLifecycleCallbacks candorActivityLifecycleCallbacks = new CandorActivityLifecycleCallbacks();
                app.registerActivityLifecycleCallbacks(candorActivityLifecycleCallbacks);
            } else {
                Log.i(TAG, "Context is not an Application, Candor will not automatically show A/B test experiments. We won't be able to automatically flush on an app background.");
            }
        }
    }

    public static Variant getExperiment(String experimentKey) {

        CandorDBHelper dbHelper = new CandorDBHelper(context);
        Variant variant = dbHelper.getVariant(experimentKey);
        return variant;
    }

    public static void track(Context context, String eventName, JSONObject properties) {
        CandorDBHelper dbHelper = new CandorDBHelper(context);
        Event event = new Event(eventName, properties);

        dbHelper.saveEvent(event);
    }

}
