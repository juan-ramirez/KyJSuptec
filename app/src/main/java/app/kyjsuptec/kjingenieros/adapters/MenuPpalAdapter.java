package app.kyjsuptec.kjingenieros.adapters;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import app.kyjsuptec.kjingenieros.helpers.DataBaseHelper;
import app.kyjsuptec.kjingenieros.R;

public class MenuPpalAdapter extends ArrayAdapter<String> {

	SQLiteDatabase database;

	private Context context;
	private ArrayList<String> values;
	private String from;

	public MenuPpalAdapter(Context context, ArrayList<String> values,
			String from) {
		super(context, R.layout.row_layout_adapter_proyectos_formularios,
				values);
		this.values = values;
		this.context = context;
		this.from = from;
		inicializarBD();
	}

	public void inicializarBD() {

		DataBaseHelper dbHelper = new DataBaseHelper(context);

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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(
				R.layout.row_layout_adapter_proyectos_formularios, parent,
				false);

		final int pos = position;

		Button buttonEliminar = (Button) rowView
				.findViewById(R.id.buttonEliminar);

		TextView txtViewMenuPpalOpcion = (TextView) rowView
				.findViewById(R.id.txtViewMenuPpalOpcion);

		txtViewMenuPpalOpcion.setText(values.get(position));
		buttonEliminar.setText("Eliminar");

		buttonEliminar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// REMOVE THE ACTION FROM THE ADAPTER'S ARRAYLIST

				showMessage();

			}

			private void showMessage() {
				AlertDialog.Builder alert = new AlertDialog.Builder(context);
				alert.setTitle("Confirmar");
				alert.setMessage("�Est� seguro que desea eliminar este elemento?, recuerde que este proceso es irreversible");

				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								if (from.equals("Usuarios")) {
									eliminarUsuario();

								} else if (from.equals("Formularios")) {
									eliminarFormulario();
								}
							}

						});
				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						});
				alert.show();
			}

			private void eliminarUsuario() {
				String query = "delete from usuarios where nombre_usuario = '"
						+ values.get(pos) + "'";
				database.execSQL(query);
				values.remove(pos);
				notifyDataSetChanged();
				Toast.makeText(context, "Usuario eliminado", Toast.LENGTH_SHORT)
						.show();
			}

			@SuppressWarnings("unchecked")
			private void eliminarFormulario() {
				SharedPreferences mPrefs = context.getSharedPreferences(
						"my_prefs", Context.MODE_PRIVATE);
				Gson gson = new Gson();
				String jsonFormularios = mPrefs.getString("Formularios", "");

				ArrayList<ArrayList<String>> formularios = gson.fromJson(
						jsonFormularios, ArrayList.class);

				for (int i = 0; i < formularios.size(); i++) {
					if (values.get(pos).equals(formularios.get(i).get(0))) {
						formularios.remove(i);
						values.remove(pos);
					}
				}

				Editor prefsEditor = mPrefs.edit();
				String json = gson.toJson(formularios);

				Log.e("JSON: ", json);

				prefsEditor.putString("Formularios", json);
				Log.e("Commited: ", String.valueOf(prefsEditor.commit()));
				notifyDataSetChanged();
				Toast.makeText(context, "Formulario eliminado",
						Toast.LENGTH_SHORT).show();
			}

		});

		return rowView;
	}

}
