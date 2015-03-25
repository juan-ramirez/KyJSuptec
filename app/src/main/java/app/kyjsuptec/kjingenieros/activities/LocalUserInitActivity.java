package app.kyjsuptec.kjingenieros.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import app.kyjsuptec.kjingenieros.R;
import app.kyjsuptec.kjingenieros.controllers.UserManager;


public class LocalUserInitActivity extends Activity {


    private EditText mProjectView;
    private EditText mCheckedView;
    private EditText mApprovedView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_user_init);


        mProjectView = (EditText) findViewById(R.id.proyecto);
        mCheckedView = (EditText) findViewById(R.id.reviso);
        mApprovedView = (EditText) findViewById(R.id.aprobo);

        Button mButtonContinuar = (Button) findViewById(R.id.continuar);
        mButtonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSave();
            }
        });

    }

    public void attemptSave() {

        // Reset errors.
        mProjectView.setError(null);
        mCheckedView.setError(null);
        mApprovedView.setError(null);

        // Store values at the time of the login attempt.
        String proyecto = mProjectView.getText().toString().trim();
        String reviso = mCheckedView.getText().toString().trim();
        String aprobo = mApprovedView.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid project, if the user entered one.
        if (TextUtils.isEmpty(proyecto)) {
            mProjectView.setError(getString(R.string.error_field_required));
            focusView = mProjectView;
            cancel = true;
        }

        // Check for a valid checked, if the user entered one.
        if (TextUtils.isEmpty(reviso)) {
            mCheckedView.setError(getString(R.string.error_field_required));
            focusView = mCheckedView;
            cancel = true;
        }

        // Check for a valid checked, if the user entered one.
        if (TextUtils.isEmpty(aprobo)) {
            mApprovedView.setError(getString(R.string.error_field_required));
            focusView = mApprovedView;
            cancel = true;
        }


        if (cancel) {
            focusView.requestFocus();
        } else {
            saveData(proyecto, reviso, aprobo);
            goToMainActivity();
        }
    }

    private void goToMainActivity() {
        Intent generalIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(generalIntent);
        finish();
    }

    private void saveData(String proyecto, String reviso, String aprobo) {
        UserManager.setProyecto(this, proyecto);
        UserManager.setAprobo(this, reviso);
        UserManager.setReviso(this, aprobo);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_local_user_init, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
