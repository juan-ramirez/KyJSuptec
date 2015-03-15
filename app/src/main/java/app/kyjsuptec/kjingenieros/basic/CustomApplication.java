package app.kyjsuptec.kjingenieros.basic;

import android.app.Application;

import com.parse.Parse;

import app.kyjsuptec.kjingenieros.R;

/**
 * Created by USER on 14/03/2015.
 */
public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initializeParse();
    }

    private void initializeParse() {
        // Required - Initialize the Parse SDK
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));
    }
}
