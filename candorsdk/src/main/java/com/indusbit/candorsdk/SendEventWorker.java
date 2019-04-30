package com.indusbit.candorsdk;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.google.gson.Gson;
import retrofit2.Call;

public class SendEventWorker extends Worker {

    private static final String TAG = "SendEventWorker";

    public SendEventWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        final Context context = getApplicationContext();
        String accountToken = getInputData().getString(CandorHelper.ACCOUNT_TOKEN_KEY);
        final String userId = getInputData().getString(CandorHelper.USER_ID_KEY);
        final String experimentKey = getInputData().getString(CandorHelper.EXPERIMENT_KEY);
        final String eventName = getInputData().getString(CandorHelper.EVENT_NAME_KEY);
        final String eventProperties = getInputData().getString(CandorHelper.EVENT_PROPERTIES_KEY);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        EventBody body = new EventBody();
        Event event = new Event("name", eventProperties);
        body.setEvent(event);

        Call<Void> call = apiService.sendEvents(accountToken, userId, experimentKey, body);

        try {
            do {
                call.execute().body();
            } while (false);

            Log.d(TAG, "event sent");

        } catch (Exception e) {


            Log.e(TAG, e.toString());
            return Result.retry();
        }

        return Result.success();
    }
}
