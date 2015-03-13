package app.kyjsuptec.kjingenieros;

import java.io.IOException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CrearModificarUsuarioActivity extends Activity {

	private boolean isModificar;
	SQLiteDatabase database;

	private EditText editTextUsuariosNombre;
	private EditText editTextUsuariosPassword;
	private EditText editTextUsuariosPasswordConfirmar;

	private EditText editTextUsuariosPasswordOld;
	private EditText editTextUsuariosPasswordNew;
	private EditText editTextUsuariosPasswordConfirmarNew;
	private TextView textViewUsuariosNombre;
	private String usuarioNombre;

	private String nombreUsuario;
	private String password;
	private String passwordConfirmar;

	private String passwordNew;
	private String passwordConfirmarnew;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		obtenerProcedencia();

		if (isModificar) {
			setContentView(R.layout.activity_usuarios_modificar);
			usuarioNombre = getIntent().getExtras().getString("usuarioNombre");
			inicializarObjetosModificar();
			setTitle("Modificar usuario");
		} else {
			setContentView(R.layout.activity_usuarios_crear);
			inicializarObjetosCrear();
			setTitle("Crear usuario");
		}

		inicializarBD();

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

	private void inicializarObjetosModificar() {
		editTextUsuariosPasswordOld = (EditText) findViewById(R.id.editTextUsuariosPasswordOld);
		editTextUsuariosPasswordOld.setVisibility(View.GONE);
		editTextUsuariosPasswordNew = (EditText) findViewById(R.id.editTextUsuariosPasswordNew);
		editTextUsuariosPasswordConfirmarNew = (EditText) findViewById(R.id.editTextUsuariosPasswordConfirmarNew);
		textViewUsuariosNombre = (TextView) findViewById(R.id.textViewUsuariosNombre);
		textViewUsuariosNombre.setText(usuarioNombre);
	}

	private void inicializarObjetosCrear() {
		editTextUsuariosNombre = (EditText) findViewById(R.id.editTextUsuariosNombre);
		editTextUsuariosPassword = (EditText) findViewById(R.id.editTextUsuariosPassword);
		editTextUsuariosPasswordConfirmar = (EditText) findViewById(R.id.editTextUsuariosPasswordConfirmar);
	}

	private void obtenerProcedencia() {
		isModificar = getIntent().getExtras().getBoolean("isModificar");
	}

	public void crearUsuario(View v) {
		obtenerDatosCrear();
		if (verificarVaciosCrear()) {
			Toast.makeText(this,
					"Alguno de los campos est� vac�o, por favor ingr�selo",
					Toast.LENGTH_SHORT).show();
		} else {
			if (!verificarPasswordMatch()) {
				Toast.makeText(this,
						"Las contrase�as no coinciden, ingr�selas de nuevo",
						Toast.LENGTH_SHORT).show();
				editTextUsuariosPassword.setText("");
				editTextUsuariosPasswordConfirmar.setText("");
			} else {
				try {
					almacenarDatos();
					Intent generalIntent = new Intent(getApplicationContext(),
							AdministrarUsuariosActivity.class);
					startActivity(generalIntent);
					finish();

				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(
							getApplicationContext(),
							"El usuario ya existe, por favor elija otro nombre",
							Toast.LENGTH_SHORT).show();
				}

			}
		}

	}

	private void obtenerDatosCrear() {

		nombreUsuario = editTextUsuariosNombre.getText().toString();
		password = editTextUsuariosPassword.getText().toString();
		passwordConfirmar = editTextUsuariosPasswordConfirmar.getText()
				.toString();

	}

	private boolean verificarVaciosCrear() {
		return nombreUsuario.equals("") || password.equals("")
				|| passwordConfirmar.equals("");
	}

	private boolean verificarPasswordMatch() {
		return password.equals(passwordConfirmar);
	}

	private void almacenarDatos() {

		String query = "insert into usuarios values ('" + nombreUsuario + "','"
				+ password + "','usuario')";
		database.execSQL(query);
	}

	public void modificarUsuario(View v) {
		obtenerDatosModificar();
		if (verificarVaciosModificar()) {
			Toast.makeText(this,
					"Alguno de los campos est� vac�o, por favor ingr�selo",
					Toast.LENGTH_SHORT).show();
		} else {
			if (!verificarPasswordMatchModificar()) {
				Toast.makeText(this,
						"Las contrase�as no coinciden, ingr�selas de nuevo",
						Toast.LENGTH_SHORT).show();
				editTextUsuariosPasswordNew.setText("");
				editTextUsuariosPasswordConfirmarNew.setText("");
			} else {
				modificarDatos();
				Intent generalIntent = new Intent(getApplicationContext(),
						AdministrarUsuariosActivity.class);
				startActivity(generalIntent);
				finish();
			}
		}
	}

	private void obtenerDatosModificar() {

		passwordNew = editTextUsuariosPasswordNew.getText().toString();
		passwordConfirmarnew = editTextUsuariosPasswordConfirmarNew.getText()
				.toString();

	}

	private boolean verificarVaciosModificar() {
		return passwordNew.equals("") || passwordConfirmarnew.equals("");
	}

	private boolean verificarPasswordMatchModificar() {
		return passwordNew.equals(passwordConfirmarnew);
	}

	private void modificarDatos() {
		String query = "update usuarios set password = '" + passwordNew
				+ "' where nombre_usuario = '" + usuarioNombre + "'";
		database.execSQL(query);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_crear_modificar_usuario, menu);
		return true;
	}

	@Override
	protected void onStop() {
		database.close();
		super.onStop();
	}

}
