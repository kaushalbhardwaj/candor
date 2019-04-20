package com.indusbit.candorsdk;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
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
        String accountToken = null;

        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = info.metaData;
            accountToken = bundle.getString("io.candor.AccountToken");
            Log.d(TAG, accountToken);

        } catch (Exception exception) {

            Log.e(TAG, "please add the account token");
            return Result.failure();
        }

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<Experiments> call = apiService.getExperiments(accountToken, "kaushal");
        try {

            call.enqueue(new Callback<Experiments>() {
                @Override
                public void onResponse(Call<Experiments> call, Response<Experiments> response) {

                    CandorDBHelper dbHelper = new CandorDBHelper(context);
                    dbHelper.saveExperiments(response.body().experiments);
                    Log.d(TAG, "worker successful");
                }

                @Override
                public void onFailure(Call<Experiments> call, Throwable t) {
                    Log.d(TAG, "error is-" + t.toString());

                }
            });

        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return Result.failure();
        }

        return Result.success();
    }
}
