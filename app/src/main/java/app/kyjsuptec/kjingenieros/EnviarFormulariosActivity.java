package app.kyjsuptec.kjingenieros;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;


public class EnviarFormulariosActivity extends Activity {

	private ArrayList<String> arrayListMenu;
	private ListView listViewMenu;
	private FormularioAdapter adapter;
	private ArrayList<ArrayList<String>> formularios = null;
	String fecha;
	String code;
	private ArrayList<ArrayList<String>> datos = new ArrayList<ArrayList<String>>();
	private String nombreFormulario;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enviar_formularios);
		inicializarObjetos();

		fecha = getIntent().getStringExtra("date");
		code = getIntent().getStringExtra("code");
		nombreFormulario = getIntent().getStringExtra("nombreFormulario");

		setTitle(code + " - " + fecha);
		refrescarDatos();
	}

	private void inicializarObjetos() {
		arrayListMenu = new ArrayList<String>();
		adapter = new FormularioAdapter(this, arrayListMenu);

		listViewMenu = (ListView) findViewById(R.id.listViewFormularios);

		listViewMenu.setAdapter(adapter);

		listViewMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
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

			if (element.equals(nombreFormulario)) {
				if (!incompleto.equals("Incompleto")) {
					String now = formularios.get(i).get(1);
					String date = now.substring(0, 10);
					if (date.equals(fecha)) {
						if (!arrayListMenu.contains(now)) {
							datos.add(formularios.get(i));
							arrayListMenu.add(now);
						}

					}
				}
			}

		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_enviar, menu);
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
		case R.id.action_email:
			if (arrayListMenu.size() > 0) {
				ArrayList<ArrayList<String>> enviar = new ArrayList<ArrayList<String>>();
				for (int i = 0; i < adapter.getChecked().length; i++) {
					if (adapter.getChecked()[i]) {
						enviar.add(datos.get(i));
					}
				}
				if (enviar.size() == 0) {
					Toast.makeText(this,
							"No ha seleccionado ning�n formulario",
							Toast.LENGTH_SHORT).show();
				} else {
					enviarMail(enviar);
				}
			} else {
				Toast.makeText(this,
						"Todos los formularios ya han sido enviados",
						Toast.LENGTH_SHORT).show();
			}

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void enviarMail(ArrayList<ArrayList<String>> enviar) {
		String date = now();
		String fileName = enviar.get(0).get(0) + " " + date + ".pdf";
		try {
			PDFWriterFormulario.savePDF(enviar, fileName, this);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		enviarPDfemail(fileName);
		for (int i = 0; i < enviar.size(); i++) {
			formularios.remove(enviar.get(i));
		}

		SharedPreferences mPrefs = getSharedPreferences("my_prefs",
				MODE_PRIVATE);
		Gson gson = new Gson();
		Editor prefsEditor = mPrefs.edit();
		String json = gson.toJson(formularios);

		// Log.e("JSON: ", json);

		prefsEditor.putString("Formularios", json);
		Log.e("Commited: ", String.valueOf(prefsEditor.commit()));

	}

	public static String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy - hh:mm:ss a");
		return sdf.format(cal.getTime());
	}

	private void enviarPDfemail(String fileName) {
		Intent sendIntent;

		sendIntent = new Intent(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Observaciones de obra");
		sendIntent.putExtra(Intent.EXTRA_TEXT, "Cordial saludo");
		sendIntent.putExtra(Intent.EXTRA_STREAM,
				Uri.parse("file:///mnt/sdcard/" + fileName));
		sendIntent.setType("application/pdf");

		startActivityForResult(Intent.createChooser(sendIntent, "Send Mail"), 1);

	}

	public void onActivityResult(int requestCode, int resultCode,
			Intent imageReturnedIntent) {
		refrescarDatos();
	}

}
