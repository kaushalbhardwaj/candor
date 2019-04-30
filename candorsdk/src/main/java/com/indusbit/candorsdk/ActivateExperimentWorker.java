package com.indusbit.candorsdk;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import retrofit2.Call;

public class ActivateExperimentWorker extends Worker {

    private static final String TAG = "ActivateExpWorker";

    public ActivateExperimentWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        final Context context = getApplicationContext();
        String accountToken = getInputData().getString(CandorHelper.ACCOUNT_TOKEN_KEY);
        final String userId = getInputData().getString(CandorHelper.USER_ID_KEY);
        final String experimentKey = getInputData().getString(CandorHelper.EXPERIMENT_KEY);
        final String variantKey = getInputData().getString(CandorHelper.VARIANT_KEY);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<Void> call = apiService.activateExperiment(accountToken, userId, experimentKey, variantKey);

        try {

            call.execute().body();
            Log.d(TAG, "experiment activated");
            return Result.success();


        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return Result.retry();
        }

    }
}
