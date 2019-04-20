package com.indusbit.candorsdk;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

public class ConfigurationChecker {

    public static String TAG = "Candor.ConfigurationChecker";

    public static boolean checkBasicConfiguration(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        final String packageName = context.getPackageName();

        if (packageManager == null || packageName == null) {
            Log.w(TAG, "Can't check configuration when using a Context with null packageManager or packageName");
            return false;
        }
        if (PackageManager.PERMISSION_GRANTED != packageManager.checkPermission("android.permission.INTERNET", packageName)) {
            Log.w(TAG, "Package does not have permission android.permission.INTERNET - Mixpanel will not work at all!");
            Log.i(TAG, "You can fix this by adding the following to your AndroidManifest.xml file:\n" +
                    "<uses-permission android:name=\"android.permission.INTERNET\" />");
            return false;
        }

        return true;
    }
}

