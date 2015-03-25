package app.kyjsuptec.kjingenieros.basic;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.parse.Parse;

import app.kyjsuptec.kjingenieros.R;
import app.kyjsuptec.kjingenieros.controllers.UserManager;

/**
 * Created by USER on 14/03/2015.
 */
public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initializeParse();
        initializeLocalUsers();
    }

    private void initializeLocalUsers() {
        SharedPreferences settings = getSharedPreferences("mAppData", 0);

        if (settings.getBoolean("isFirstExecution", true)) {
            Log.e("First_Time?", "First time");

            UserManager.setUpLocalAdmin(this, getString(R.string.local_admin_name), getString(R.string.local_admin_default_password));
            UserManager.setUpLocalUser(this, getString(R.string.local_user_name), getString(R.string.local_user_default_password));

            settings.edit().putBoolean("isFirstExecution", false).commit();
        }
    }

    private void initializeParse() {
        // Required - Initialize the Parse SDK
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));
    }
}
