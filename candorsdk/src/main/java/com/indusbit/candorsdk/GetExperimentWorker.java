package com.indusbit.candorsdk;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetExperimentWorker extends Worker {

    private static final String TAG = "GetExperimentWorker";

    public GetExperimentWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        final Context context = getApplicationContext();
        String accountToken = getInputData().getString(CandorHelper.ACCOUNT_TOKEN_KEY);
        final String userId = getInputData().getString(CandorHelper.USER_ID_KEY);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<Experiments> call = apiService.getExperiments(accountToken, userId);

        try {

            Experiments experiments = call.execute().body();

            CandorDBHelper dbHelper = new CandorDBHelper(context);
            dbHelper.deleteExperiments();
            String experimentJson = new Gson().toJson(experiments);

            dbHelper.saveExperiments(context, experimentJson, userId);
            Log.d(TAG, "worker successful -- " + experiments.toString());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return Result.retry();
        }

        return Result.success();
    }
}
