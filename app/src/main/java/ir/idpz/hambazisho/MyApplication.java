package ir.idpz.hambazisho;

import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.vaslSdk.VaslAppSDK;

public class MyApplication extends MultiDexApplication {

    private static VaslAppSDK vaslAppSDK;

    private static final String TAG = "MyApplication";

    static {
        System.loadLibrary("native-lib");
    }

    public native String getClientID();

    public native String getClientSecret();

    public native String getSite();

    public native String getUserName();

    public native String getPassWord();

    public static VaslAppSDK getVaslAppClient() {
        return vaslAppSDK;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            vaslAppSDK = new VaslAppSDK
                    .Builder(getApplicationContext(), getClientID())
                    .setClientId(getClientID())
                    .setClientSecret(getClientSecret())
                    .setUserName(getUserName())
                    .setPassword(getPassWord())
                    .setSite(getSite())
                    .build();

            Log.i(TAG, "Init ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}