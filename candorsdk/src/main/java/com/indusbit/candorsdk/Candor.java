package com.indusbit.candorsdk;

import android.content.Context;
import androidx.annotation.Nullable;
import org.json.JSONObject;

/**
 * Core class for interacting with Candor experimentation platform
 *
 * <p>
 * your main application activity and your Candor API token(//TODO dont know what type of token it is)
 * as arguments to get an instance you can use to report how users are using your
 * application.
 * </p>
 *
 * <div>
 * Once you have an instance, then
 * <p>
 * you can activate an experiment using {@link #getExperiment(String)} using
 * the key of the experiment and the userId. This method will return the different variants of the specified experiment
 * </p>
 * <p>
 * you can track the various events using {@link #track(String, JSONObject)} with the name of the event
 * and the properties associated with the event you want to track
 * </p>
 * </div>
 */
public class Candor {

    private final String TAG = "Candor";
    private Context context = null;
    private CandorHelper candorHelper = null;
    private String accountToken = null;
    private ExperimentFetchedListener listener = null;


    /**
     * You shouldn't instantiate Candor Experiments directly.
     * Use Candor.getInstance to get an instance.
     */

    /**
     * Get the instance of Candor associated with your Candor project token
     *
     * <p>
     * Use getInstance to get a reference to a shared instance of Candor you
     * can use to activate experiment and to track events
     * </p>
     *
     * @param context The application context you are tracking
     * @return an instance of Candor associated with your project
     */

    private Candor(Context context, String accountToken) {
        this.context = context;
        this.accountToken = accountToken;
    }

    @Nullable
    public static Candor initialize(Context context, String accountToken) {

        if (context == null || accountToken == null)
            return null;

        return new Candor(context, accountToken);

    }

    @Nullable
    public static Candor initialize(Context context, String accountToken, String userId) {

        Candor candor = initialize(context, accountToken);
        if (candor == null || userId == null)
            return null;

        candor.signIn(userId);
        return candor;
    }

    public void signIn(String userId) {
        if (userId == null) {
            candorHelper = null;
            return;
        }

        candorHelper = CandorHelper.initialize(context, accountToken, userId);

    }

    public void signOut() {
        candorHelper = null;

    }

    /**
     * Activate any experiment on this platform using the key of the experiment
     *
     * <p>
     * To activate the experiment pass the key of the experiment and the userId
     * and in return you will get different variants associated with the given
     * experiment. You can then further decide which variant you want to experiment in the app
     * </p>
     *
     * @param experimentKey The key of the experiment you want to activate
     */
    public Experiment getExperiment(String experimentKey) {
        if (candorHelper == null)
            return null;

        return candorHelper.getExperiment(context, experimentKey);
    }

    /**
     * Track an event
     *
     * <p>
     * Every call to track event results in a data point sent to Candor. These data points are used
     * to generate the reports regarding the result of various experiments. Events have event name and properties which
     * describe the event
     * </p>
     *
     * @param eventName  The name of the event to send
     * @param properties A Map containing the key value pairs of the properties to include in this event.
     *                   Pass null if no extra properties exist
     */

    public void track(String experimentKey, String eventName, JSONObject properties) {

        if (candorHelper == null || eventName == null || properties == null)
            return;

        candorHelper.track(context, experimentKey, eventName, properties);

        return;

    }

    public void setExperimentFetchedListener(ExperimentFetchedListener listener) {
        if (listener == null || candorHelper == null)
            return;

        candorHelper.setExperimentFetchedListener(listener);

    }
}
