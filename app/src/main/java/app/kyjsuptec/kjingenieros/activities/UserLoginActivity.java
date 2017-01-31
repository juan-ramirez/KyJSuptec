package app.kyjsuptec.kjingenieros.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import app.kyjsuptec.kjingenieros.R;
import app.kyjsuptec.kjingenieros.controllers.NetworkManager;
import app.kyjsuptec.kjingenieros.controllers.UserManager;
import app.kyjsuptec.kjingenieros.model.User;

import static android.content.ContentValues.TAG;


/**
 * A login screen that offers login via email/password.
 */
public class UserLoginActivity extends Activity {

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private CheckBox mRememberView;
    private View mProgressView;
    private View mLoginFormView;
    private boolean useLocalUser;
    private boolean cancelLogin;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        getActionBar().hide();

        //Setup Firebase
        setupFirebase();

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mRememberView = (CheckBox) findViewById(R.id.checkBoxRemember);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        if (UserManager.isSesionActive(this) && NetworkManager.isConnected(getApplicationContext())) {
            showProgress(true);
            mAuthTask = new UserLoginTask(UserManager.getUser(this), UserManager.getPassword(this));
            mAuthTask.execute((Void) null);
        }

    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    private void shouldloginLocalUser(final String email, final String password) {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Log.e("Using Local user", "Yeah");
                        useLocalUser = true;
                        showProgress(true);
                        mAuthTask = new UserLoginTask(email, password);
                        mAuthTask.execute((Void) null);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        cancelLogin = true;
                        dialog.cancel();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(UserLoginActivity.this);
        builder.setMessage(R.string.show_dialog).setPositiveButton("Si", dialogClickListener)
               .setNegativeButton("No", dialogClickListener).show();

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            if (NetworkManager.isConnected(getApplicationContext())) {
                useLocalUser = false;
                showProgress(true);
                mAuth.signInWithEmailAndPassword(email, password)
                     .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {

                             if (task.isSuccessful()) {
                                 String userUUID = task.getResult().getUser().getUid();
                                 Log.d("USER", userUUID);

                                 final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                 DatabaseReference reference = database.getReference();

                                 Query query = reference.child("users").orderByChild("uuid").equalTo(userUUID);
                                 query.addListenerForSingleValueEvent(new ValueEventListener() {
                                     @Override
                                     public void onDataChange(DataSnapshot dataSnapshot) {
                                         if (dataSnapshot.exists()) {
                                             for (DataSnapshot userDataSnapshot : dataSnapshot.getChildren()) {
                                                 User mUser = userDataSnapshot.getValue(User.class);
                                                 setUserElements(mUser);

                                                 Intent generalIntent = new Intent(getApplicationContext(), MainActivity.class);
                                                 startActivity(generalIntent);
                                                 finish();
                                             }
                                         }

                                     }

                                     @Override
                                     public void onCancelled(DatabaseError databaseError) {

                                     }
                                 });
                             }

                             if (!task.isSuccessful()) {
                                 Log.w(TAG, "signInWithEmail", task.getException());
                                 Toast.makeText(getApplicationContext(), getString(R.string.wrong_data), Toast.LENGTH_SHORT).show();
                             }

                             showProgress(false);
                         }
                     });
            } else {
                shouldloginLocalUser(email, password);
            }

        }
    }


    private void setUserElements(User mUser) {
        UserManager.setProyecto(getApplicationContext(), mUser.getProject());
        UserManager.setReviso(getApplicationContext(), mUser.getChecked());
        UserManager.setAprobo(getApplicationContext(), mUser.getApproved());
        UserManager.setIsAdmin(getApplicationContext(), mUser.getIsAdmin());
    }

    private boolean isEmailValid(String email) {
        return email.matches(EMAIL_PATTERN);
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        boolean authSuccess = false;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (useLocalUser) {
                if (mEmail.equals(getString(R.string.local_admin_name))) {
                    if (mPassword.equals(UserManager.getLocalAdminPassword(getApplicationContext()))) {
                        UserManager.setIsAdmin(getApplicationContext(), true);
                        return true;
                    } else {
                        return false;
                    }
                } else if (mEmail.equals(getString(R.string.local_user_name))) {
                    if (mPassword.equals(UserManager.getLocalUserPassword(getApplicationContext()))) {
                        UserManager.setIsAdmin(getApplicationContext(), false);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return authSuccess;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                if (mRememberView.isChecked() && NetworkManager.isConnected(getApplicationContext())) {
                    UserManager.setActiveSesion(getApplicationContext(), mEmail, mPassword);
                }
                Log.e("Success", "Login");
                Intent generalIntent;
                if (useLocalUser) {
                    UserManager.setInactiveSesion(getApplicationContext());
                    generalIntent = new Intent(getApplicationContext(), LocalUserInitActivity.class);
                    startActivity(generalIntent);
                    finish();
                }
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.wrong_data), Toast.LENGTH_SHORT).show();
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
                UserManager.setInactiveSesion(getApplicationContext());
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}



