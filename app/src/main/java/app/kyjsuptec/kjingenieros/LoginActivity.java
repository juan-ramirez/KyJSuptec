package app.kyjsuptec.kjingenieros;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.io.IOException;

public class LoginActivity extends Activity {

    SQLiteDatabase database;
    private EditText editTextLoginUsuario;
    private EditText editTextLoginPassword;
    private String usuario;
    private String password;
    private String perfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getActionBar();

        //Parse

        // Enable Local Datastore.
        ParseUser.logInInBackground("Juan_Ramirez", "juanchogg", new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Log.e("Project", user.getString("project"));
                } else {
                    Log.e("Failed", e.toString() + " " + e.getCode());
                }
            }
        });

        /*actionBar.hide();
        inicializarBD();
		inicializarObjetos();
*/
    }

    public void inicializarBD() {

        DataBaseHelper dbHelper = new DataBaseHelper(this);

        try {

            dbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            dbHelper.openDataBase();

        } catch (SQLException sqle) {

            throw sqle;

        }
        database = dbHelper.myDataBase;
    }

    private void inicializarObjetos() {
        editTextLoginUsuario = (EditText) findViewById(R.id.editTextLoginUsuario);
        editTextLoginPassword = (EditText) findViewById(R.id.editTextLoginPassword);
    }

    private void validarUsuario() {
        verificarVacios();
        obtenerCredenciales();
        if (validarCredenciales()) {
            Intent generalIntent = new Intent(getApplicationContext(),MainActivity.class);
            generalIntent.putExtra("perfil", perfil);
            startActivity(generalIntent);
            database.close();
            finish();
        } else {
            editTextLoginUsuario.setText("");
            editTextLoginPassword.setText("");
            Toast.makeText(this, "Usuario o contrase�a err�neos",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean verificarVacios() {
        return editTextLoginUsuario.equals("")
                || editTextLoginPassword.equals("");
    }

    private void obtenerCredenciales() {
        usuario = editTextLoginUsuario.getText().toString().trim();
        password = editTextLoginPassword.getText().toString().trim();
    }

    private boolean validarCredenciales() {
        String query = "select * from usuarios where nombre_usuario = '"
                + usuario + "' and password = '" + password + "'";
        Cursor c = database.rawQuery(query, null);
        c.moveToFirst();
        if (c.getCount() > 0) {
            perfil = c.getString(c.getColumnIndex("perfil"));
            Toast.makeText(getApplicationContext(), perfil, Toast.LENGTH_SHORT)
                    .show();
            c.close();
            return true;
        } else {
            c.close();
            return false;
        }
    }

    public void doLogin(View v) {
        validarUsuario();
    }
}
