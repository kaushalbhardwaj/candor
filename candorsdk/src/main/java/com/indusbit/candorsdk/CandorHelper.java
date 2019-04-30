package com.indusbit.candorsdk;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CandorHelper {

    private final String TAG = "CandorHelper";
    public static final String ACCOUNT_TOKEN_KEY = "account_token_key";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String VARIANT_KEY = "variant_key";
    public static final String EXPERIMENT_KEY = "experiment_key";
    public static final String EVENT_NAME_KEY = "event_name_key";
    public static final String EVENT_PROPERTIES_KEY = "event_properties_key";

    private Experiments experiments = null;
    private String userId = null;
    private String accountToken = null;
    private Map<String, String> deviceInfo = null;
    CandorDBHelper dbHelper = null;

    private CandorHelper(Context context, String accountToken, String userId) {

        this.userId = userId;
        this.deviceInfo = getDeviceInfo(context);
        this.dbHelper = new CandorDBHelper(context);
        this.accountToken = accountToken;
        registerActivityLifecycleCallbacks(context);
        registerExperimentWorker(context, accountToken, userId);

    }

    public static CandorHelper initialize(final Context context, String accountToken, String userId) {
        return new CandorHelper(context, accountToken, userId);
    }

    private void registerActivityLifecycleCallbacks(Context context) {
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

    private void registerExperimentWorker(Context context, String accountToken, String userId) {
        Data.Builder builder = new Data.Builder();
        builder.putString(ACCOUNT_TOKEN_KEY, accountToken);
        builder.putString(USER_ID_KEY, userId);
        Data data = builder.build();

        WorkManager workManager = WorkManager.getInstance();
        OneTimeWorkRequest getRequest = new OneTimeWorkRequest.Builder(GetExperimentWorker.class)
                .setInputData(data)
                .build();
        workManager.enqueue(getRequest);

    }

    public Experiment getExperiment(Context context, String experimentKey) {

        if (userId == null || experimentKey == null)
            return null;

        if (experiments != null) {
            Log.d(TAG, "inside cache data" + experiments.toString());
            Experiment experiment = getExperimentFromExperiments(experimentKey);
            activateExperiment(experiment);
            return experiment;

        }

        CandorDBHelper dbHelper = new CandorDBHelper(context);
        experiments = dbHelper.getExperiments(userId);
        if (experiments == null)
            return null;

        Log.d(TAG, "new data retrieved" + experiments.toString());
        Experiment experiment = getExperimentFromExperiments(experimentKey);
        activateExperiment(experiment);
        return experiment;
    }

    private void activateExperiment(Experiment experiment) {

        Data.Builder builder = new Data.Builder();
        builder.putString(ACCOUNT_TOKEN_KEY, accountToken);
        builder.putString(USER_ID_KEY, userId);
        builder.putString(EXPERIMENT_KEY, experiment.key);
        builder.putString(VARIANT_KEY, experiment.variant.key);
        Data data = builder.build();
        Log.d("ActivateExpWorker", "inside");

        WorkManager workManager = WorkManager.getInstance();
        OneTimeWorkRequest getRequest = new OneTimeWorkRequest.Builder(ActivateExperimentWorker.class)
                .setInputData(data)
                .build();
        workManager.enqueue(getRequest);

    }


    private Experiment getExperimentFromExperiments(String experimentKey) {

        for (Experiment experiment : experiments.getExperiments()) {

            if (experiment.key.equals(experimentKey))
                return experiment;

        }

        return null;
    }

    private Map<String, String> getDeviceInfo(Context context) {
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

    public void track(Context context, String experimentKey, String eventName, JSONObject properties) {

        Data.Builder builder = new Data.Builder();
        builder.putString(ACCOUNT_TOKEN_KEY, accountToken);
        builder.putString(USER_ID_KEY, userId);
        builder.putString(EXPERIMENT_KEY, experimentKey);
        builder.putString(EVENT_NAME_KEY, eventName);
        builder.putString(EVENT_PROPERTIES_KEY, properties.toString());
        Data data = builder.build();

        WorkManager workManager = WorkManager.getInstance();
        OneTimeWorkRequest getRequest = new OneTimeWorkRequest.Builder(SendEventWorker.class)
                .setInputData(data)
                .build();
        workManager.enqueue(getRequest);

    }

    public void setExperimentFetchedListener(ExperimentFetchedListener listener) {
        dbHelper.setExperimentFetchedListener(listener);
    }
}
