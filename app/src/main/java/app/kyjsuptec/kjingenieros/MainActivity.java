package app.kyjsuptec.kjingenieros;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

public class MainActivity extends Activity {

	private ArrayList<String> arrayListMenu;
	private ListView listViewMenu;
	private FormularioSimpleAdapter adapter;
	private ArrayList<ArrayList<String>> formularios = null;
	private Spinner spinnerFormularios;
	private ArrayList<String> spinnerArray;
	private int mCurrentPosition;
	private ArrayList<String> mCurrentForm;
	private static String m_Text = "1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_proyectos_formularios);

		arrayListMenu = new ArrayList<String>();

		ActionBar actionBar = getActionBar();

		setTitle("Formularios");

		actionBar.setDisplayHomeAsUpEnabled(true);

		inicializarObjetos();
	}

	private void inicializarObjetos() {
		adapter = new FormularioSimpleAdapter(this, arrayListMenu);

		listViewMenu = (ListView) findViewById(R.id.listViewProyectos_Formularios);

		listViewMenu.setAdapter(adapter);

		listViewMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent generalIntent;

				if (mCurrentPosition == spinnerArray.size() - 1) {
					mCurrentForm = new ArrayList<String>();
					generalIntent = new Intent(getApplicationContext(),
							FormularioActivity.class);
					String item = adapter.getItem(position);

					for (int i = 0; i < formularios.size(); i++) {
						ArrayList<String> elemento = formularios.get(i);
						String dato = elemento.get(0) + " " + elemento.get(1);
						if (item.equals(dato)) {
							mCurrentForm = elemento;
						}
					}
					generalIntent.putExtra("esCargar", true);

					generalIntent.putExtra("numeroFormulario",
							(Integer.parseInt(mCurrentForm.get(2))));

					generalIntent.putExtra("nombreFormulario",
							mCurrentForm.get(0));

					generalIntent.putExtra("id_formulario", item);

					startActivity(generalIntent);
				} else {
					generalIntent = new Intent(getApplicationContext(),
							EnviarFormulariosActivity.class);
					generalIntent.putExtra("date", arrayListMenu.get(position));

					String code = spinnerArray.get(mCurrentPosition);
					code = code.substring(code.length() - 7, code.length());
					generalIntent.putExtra("code", code);

					String nombreFormulario = spinnerArray
							.get(mCurrentPosition);
					generalIntent
							.putExtra("nombreFormulario", nombreFormulario);

					startActivity(generalIntent);
				}
			}
		});

		spinnerFormularios = (Spinner) findViewById(R.id.spinnerFormularios);

		ArrayAdapter<String> spinnerArrayAdapter;
		spinnerArray = new ArrayList<String>();

		spinnerArray.add("EMPAQUE Y ROTULADO DE CEMENTO - FST001");
		spinnerArray.add("EVALUACI�N CONCRETO - FST002");
		spinnerArray.add("VERIFICACI�N CONDICIONES DE CIMENTACI�N - FST003");
		spinnerArray
				.add("MEZCLA, TRANSPORTE, COLOCACI�N Y CURADO DE CONCRETOS - FST004");
		spinnerArray
				.add("CONSTRUCCI�N Y RETIRO DE FORMALETAS, OBRA FALSA - FST005");
		spinnerArray.add("COLOCACI�N ACERO DE REFUERZO - FST006");
		spinnerArray.add("ACEPTACI�N DE ELEMENTOS VACIADOS - FST007");
		spinnerArray
				.add("REQUISITOS DE EJECUCI�N - MUROS Y ELEMENTOS DE MAMPOSTER�A - FST008");
		spinnerArray.add("LIBERACI�N DE ELEMENTOS - FST009");
		spinnerArray.add("INCOMPLETOS");

		spinnerArrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, spinnerArray);
		spinnerFormularios.setAdapter(spinnerArrayAdapter);
		spinnerFormularios
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						mCurrentPosition = arg2;
						refrescarDatos();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

	}

	@SuppressWarnings("unchecked")
	public void refrescarDatos() {
		arrayListMenu.clear();

		SharedPreferences mPrefs = getSharedPreferences("my_prefs",
				MODE_PRIVATE);
		Gson gson = new Gson();
		String jsonFormularios = mPrefs.getString("Formularios", "");

		if (jsonFormularios.equals("")) {
			formularios = new ArrayList<ArrayList<String>>();
		} else {
			formularios = gson.fromJson(jsonFormularios, ArrayList.class);
		}

		for (int i = 0; i < formularios.size(); i++) {
			ArrayList<String> formulario = formularios.get(i);
			String incompleto = formulario.get(formulario.size() - 4);
			String element = formulario.get(0);
			String opcion = spinnerArray.get(mCurrentPosition);
			if (element.equals(opcion)) {
				if (!incompleto.equals("Incompleto")) {
					String now = formulario.get(1);
					String date = now.substring(0, 10);
					if (!arrayListMenu.contains(date)) {
						// arrayListMenu.add(now);
						arrayListMenu.add(date);
					}
				}
			} else if (opcion.equals("INCOMPLETOS")) {
				if (incompleto.equals("Incompleto")) {
					String now = formulario.get(1);
					if (!arrayListMenu.contains(element + " " + now)) {
						// arrayListMenu.add(now);
						arrayListMenu.add(element + " " + now);
					}
				}
			}

		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		Bundle bundle = getIntent().getExtras();

		if (bundle != null) {
			String result = bundle.getString("perfil", "empty");
			if (!result.equals("administrador")) {
				menu.findItem(R.id.action_settings).setVisible(false);
			}
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(getApplicationContext(),
					AdministrarUsuariosActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_new:

			showMessageFormularios();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showMessageFormularios() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Nuevo formulario");
		alert.setMessage("Por favor escoja uno de los formularios");

		final Spinner input = new Spinner(this);
		ArrayAdapter<String> spinnerArrayAdapter;
		ArrayList<String> spinnerArray = new ArrayList<String>();

		spinnerArray.add("EMPAQUE Y ROTULADO DE CEMENTO - FST001");
		spinnerArray.add("EVALUACI�N CONCRETO - FST002");
		spinnerArray.add("VERIFICACI�N CONDICIONES DE CIMENTACI�N - FST003");
		spinnerArray
				.add("MEZCLA, TRANSPORTE, COLOCACI�N Y CURADO DE CONCRETOS - FST004");
		spinnerArray
				.add("CONSTRUCCI�N Y RETIRO DE FORMALETAS, OBRA FALSA - FST005");
		spinnerArray.add("COLOCACI�N ACERO DE REFUERZO - FST006");
		spinnerArray.add("ACEPTACI�N DE ELEMENTOS VACIADOS - FST007");
		spinnerArray
				.add("REQUISITOS DE EJECUCI�N - MUROS Y ELEMENTOS DE MAMPOSTER�A - FST008");
		spinnerArray.add("LIBERACI�N DE ELEMENTOS - FST009");
		spinnerArray.add("MEMO EXPRES");

		spinnerArrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, spinnerArray);
		input.setAdapter(spinnerArrayAdapter);

		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				if (input.getSelectedItem().toString().equals("MEMO EXPRES")) {
					generalIntent = new Intent(getApplicationContext(),
							MemoExpressActivity.class);
				} else {
					generalIntent = new Intent(getApplicationContext(),
							FormularioActivity.class);
					generalIntent.putExtra("numeroFormulario",
							(input.getSelectedItemPosition() + 1));
					generalIntent.putExtra("nombreFormulario", input
							.getSelectedItem().toString());

				}
				if ((input.getSelectedItemPosition() + 1) == 6) {
					showMessageCantidad();
				} else {
					startActivity(generalIntent);
				}

			}
		});
		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				});
		alert.show();
	}

	Intent generalIntent;

	private void showMessageCantidad() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Cantidad");
		alert.setMessage("Por favor ingrese el n�mero de barras que desea");

		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				m_Text = input.getText().toString();
				int reps = 1;
				try {
					reps = Integer.parseInt(m_Text);
				} catch (Exception e) {
					// TODO: handle exception
				}
				generalIntent.putExtra("reps", reps);
				startActivity(generalIntent);
			}
		});
		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				});
		alert.show();
	}

	public void popMessage(String text) {
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refrescarDatos();

	}

}
