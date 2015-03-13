package app.kyjsuptec.kjingenieros;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class AdministrarUsuariosActivity extends Activity {

	ArrayList<String> arrayListMenu;
	SQLiteDatabase database;
	ListView listViewMenu;
	String usuarioModificar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_proyectos_formularios);

		setTitle("Administraci�n de usuarios");

		inicializarBD();
		cargarUsuarios();
		inicializarObjetos();
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

	private void cargarUsuarios() {

		arrayListMenu = new ArrayList<String>();

		String query = "select * from usuarios where not nombre_usuario = 'K&JSUPTEC' ";
		Cursor c = database.rawQuery(query, null);
		c.moveToFirst();
		if (c.getCount() > 0) {
			while (c.isAfterLast() == false) {
				arrayListMenu.add(c.getString(c
						.getColumnIndex("nombre_usuario")));
				c.moveToNext();
			}

		}
	}

	private void inicializarObjetos() {
		listViewMenu = (ListView) findViewById(R.id.listViewProyectos_Formularios);
		MenuPpalAdapter adapter = new MenuPpalAdapter(this, arrayListMenu,
				"Usuarios");
		listViewMenu.setAdapter(adapter);

		listViewMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				usuarioModificar = arrayListMenu.get(position);
				iniciarCrearModificar(true, usuarioModificar);
			}
		});
	}

	private void iniciarCrearModificar(boolean isModificar, String usuarioNombre) {
		Intent generalIntent = new Intent(getApplicationContext(),
				CrearModificarUsuarioActivity.class);
		generalIntent.putExtra("isModificar", isModificar);
		generalIntent.putExtra("usuarioNombre", usuarioNombre);
		startActivity(generalIntent);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_crear_modificar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_new:
			iniciarCrearModificar(false, null);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onStop() {
		database.close();
		super.onStop();
	}
}
