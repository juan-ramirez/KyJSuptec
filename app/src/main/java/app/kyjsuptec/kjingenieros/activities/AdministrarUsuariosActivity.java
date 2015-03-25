package app.kyjsuptec.kjingenieros.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import app.kyjsuptec.kjingenieros.R;
import app.kyjsuptec.kjingenieros.controllers.UserManager;

public class AdministrarUsuariosActivity extends Activity {

    private EditText mLocalAdminPasswordView;
    private EditText mLocalAdminConfirmPasswordView;
    private EditText mLocalUserPasswordView;
    private EditText mLocalUserConfirmPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_usuarios);

        setTitle("Administración de usuarios");

        inicializarObjetos();
    }


    private void inicializarObjetos() {
        mLocalAdminPasswordView = (EditText) findViewById(R.id.passwordAdministradorLocal);
        mLocalAdminConfirmPasswordView = (EditText) findViewById(R.id.confirmPasswordAdminitradorLocal);
        mLocalUserPasswordView = (EditText) findViewById(R.id.passwordUsuarioLocal);
        mLocalUserConfirmPasswordView = (EditText) findViewById(R.id.confirmPasswordUsuarioLocal);


        Button mButtonUpdateLocalAdmin = (Button) findViewById(R.id.buttonActualizarAdministradorLocal);
        mButtonUpdateLocalAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser(mLocalAdminPasswordView,mLocalAdminConfirmPasswordView,true);
            }
        });

        Button mButtonUpdateLocaluser = (Button) findViewById(R.id.buttonActualizarUsuarioLocal);
        mButtonUpdateLocaluser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser(mLocalUserPasswordView,mLocalUserConfirmPasswordView,true);
            }
        });

    }

    private void updateUser(EditText mPasswordView, EditText mNewPasswordView, boolean isAdmin) {

        // Reset errors.
        mPasswordView.setError(null);
        mNewPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String oldPassword = mPasswordView.getText().toString().trim();
        String newPassword = mNewPasswordView.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(oldPassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (TextUtils.isEmpty(newPassword)) {
            mNewPasswordView.setError(getString(R.string.error_field_required));
            focusView = mNewPasswordView;
            cancel = true;
        } else if (!oldPassword.equals(newPassword)) {
            Toast.makeText(this, getString(R.string.error_passwords_dont_match), Toast.LENGTH_SHORT).show();
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if (isAdmin) {
                UserManager.updateLocalAdminPassword(this, newPassword);
            } else {
                UserManager.updateLocalUserPassword(this, newPassword);
            }
            Toast.makeText(this, getString(R.string.password_updated), Toast.LENGTH_SHORT).show();
            mPasswordView.setText("");
            mNewPasswordView.setText("");

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
