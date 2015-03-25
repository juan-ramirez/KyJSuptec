package app.kyjsuptec.kjingenieros.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import app.kyjsuptec.kjingenieros.R;
import app.kyjsuptec.kjingenieros.factory.FormularioFactory;
import app.kyjsuptec.kjingenieros.factory.FormularioViewGenerator;
import app.kyjsuptec.kjingenieros.helpers.PDFWriterFormulario;

public class FormularioActivity extends FragmentActivity {
    static Intent generalIntent;
    static ArrayList<View> arrayListElementosFormulario;
    static ArrayList<DatoFormularioFactory> arrayListFormulario;
    ArrayList<String> datosPDF;
    static ArrayList<String> form;

    private static Bitmap fotoBitmapFinal;
    private static ImageView foto1;
    private static ImageView foto2;
    private static EditText evidenciaEscrita = null;
    public static boolean isFoto1Default = true;
    public static boolean isFoto2Default = true;
    public static boolean esCargar = false;
    public static Bitmap pic1 = null;
    public static Bitmap pic2 = null;
    public static String pic1Cargar;
    public static String pic2Cargar;
    public static String evidenciaEscritaCargar;

    private static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_REQUEST_2 = 1889;
    private static String mCurrentPhotoPath;


    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        generalIntent = getIntent();

        esCargar = generalIntent.getBooleanExtra("esCargar", false);
        if (esCargar) {

            ArrayList<ArrayList<String>> formularios = null;
            Gson gson = new Gson();

            SharedPreferences mPrefs = getSharedPreferences("my_prefs",
                    MODE_PRIVATE);

            String jsonFormularios = mPrefs.getString("Formularios", "");

            if (jsonFormularios.equals("")) {
                formularios = new ArrayList<ArrayList<String>>();
            } else {
                formularios = gson.fromJson(jsonFormularios, ArrayList.class);
            }

            String item = generalIntent.getStringExtra("id_formulario");
            form = generalIntent.getStringArrayListExtra("formulario");

            for (int i = 0; i < formularios.size(); i++) {
                ArrayList<String> elemento = formularios.get(i);
                String dato = elemento.get(0) + " " + elemento.get(1);
                if (item.equals(dato)) {
                    form = elemento;
                }
            }

            formularios.remove(form);
            Editor prefsEditor = mPrefs.edit();
            String json = gson.toJson(formularios);

            // Log.e("JSON: ", json);

            prefsEditor.putString("Formularios", json);
            Log.e("Commited: ", String.valueOf(prefsEditor.commit()));

        } else {
            if (evidenciaEscrita != null) {
                evidenciaEscrita.setText("");
            }

        }

        setTitle(generalIntent.getStringExtra("nombreFormulario"));

        Tab tab = actionBar
                .newTab()
                .setText("Formulario")
                .setTabListener(
                        new TabListener<ExampleFragment>(this, "Formulario",
                                ExampleFragment.class));
        actionBar.addTab(tab);

        tab = actionBar
                .newTab()
                .setText("Observaciones")
                .setTabListener(
                        new TabListener<ExampleFragmentTwo>(this,
                                "Observaciones", ExampleFragmentTwo.class));
        actionBar.addTab(tab);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_formulario, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                prevenirPerdidaDatos();
                return (true);
            case R.id.action_save:
                // Recoger Encabezado
                datosPDF = new ArrayList<String>();
                String nombreFormulario = generalIntent
                        .getStringExtra("nombreFormulario");

                datosPDF.add(nombreFormulario);
                datosPDF.add(now());

                int numeroFormulario = generalIntent.getIntExtra(
                        "numeroFormulario", 1);
                datosPDF.add("" + numeroFormulario);

                // Recoger datos

                if (recogerDatos(false)) {
                    showMessageIncompleto();
                } else {
                    datosPDF.add("Completo");
                    obtenerEvidencia();
                }

                return (true);
        }

        return (super.onOptionsItemSelected(item));
    }

    @SuppressWarnings("unchecked")
    private void guardarFormulario() {

        // Guardar formulario
        ArrayList<ArrayList<String>> formularios = null;
        Gson gson = new Gson();

        SharedPreferences mPrefs = getSharedPreferences("my_prefs",
                MODE_PRIVATE);

        String jsonFormularios = mPrefs.getString("Formularios", "");

        if (jsonFormularios.equals("")) {
            formularios = new ArrayList<ArrayList<String>>();
        } else {
            formularios = gson.fromJson(jsonFormularios, ArrayList.class);
        }
        formularios.add(datosPDF);

        Editor prefsEditor = mPrefs.edit();
        String json = gson.toJson(formularios);

        // Log.e("JSON: ", json);

        prefsEditor.putString("Formularios", json);
        Log.e("Commited: ", String.valueOf(prefsEditor.commit()));
        finish();

    }

    private void showMessage() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Evidencia");
        alert.setMessage(getString(R.string.evidence_check));

        alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                datosPDF.add("EMPTY");
                datosPDF.add("--1");
                datosPDF.add("--2");
                guardarFormulario();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alert.show();
    }

    private void showMessageIncompleto() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Formulario Incompleto");
        alert.setMessage(getString(R.string.incomplete_check));

        alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                datosPDF = new ArrayList<String>(datosPDF.subList(0, 3));
                recogerDatos(true);
                datosPDF.add("Incompleto");
                obtenerEvidencia();

            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                showMessageDescartar();
            }
        });
        alert.show();
    }

    private void obtenerEvidencia() {

        if (evidenciaEscrita == null) {
            showMessage();
        } else {
            String texto = evidenciaEscrita.getText().toString().trim();
            if (texto.equals("") && (isFoto1Default && isFoto2Default)) {
                showMessage();
            } else {
                String evidencia1 = "--1";
                String evidencia2 = "--2";
                if (!isFoto1Default) {
                    evidencia1 = encodeTobase64(pic1);
                }
                if (!isFoto2Default) {
                    evidencia2 = encodeTobase64(pic2);
                }
                if (!texto.equals("")) {
                    Log.e("Texto", texto);
                    datosPDF.add("Observaciones: " + texto);
                } else {
                    datosPDF.add("EMPTY");
                }
                datosPDF.add(evidencia1);
                datosPDF.add(evidencia2);
                guardarFormulario();

            }
        }

    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void enviarMail(Bitmap pic1, Bitmap pic2) {

        String date = now();
        String fileName = generalIntent.getStringExtra("nombreFormulario")
                + " " + date + ".pdf";
        try {
            if (pic1 == null) {
            }
            PDFWriterFormulario.savePDF(datosPDF, pic1, pic2, fileName, this);
        } catch (UnsupportedEncodingException e) {
        }
        enviarPDfemail(fileName);
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
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Test Text");
        sendIntent.putExtra(Intent.EXTRA_STREAM,
                Uri.parse("file:///mnt/sdcard/" + fileName));
        sendIntent.setType("application/pdf");

        startActivityForResult(Intent.createChooser(sendIntent, "Send Mail"), 1);

    }

    private boolean recogerDatos(boolean isTemporal) {

        for (int i = 0; i < arrayListFormulario.size(); i++) {

            View elemento = arrayListElementosFormulario.get(i);
            String titulo = arrayListFormulario.get(i).getTitulo();

            switch (arrayListFormulario.get(i).getTipo()) {
                case 1:
                    EditText editTextFormularios = (EditText) elemento
                            .findViewById(R.id.editTextFormularios);

                    String texto = editTextFormularios.getText().toString().trim();
                    if (texto.equals("")) {
                        if (isTemporal) {
                            datosPDF.add("EMPTY");
                        } else {
                            return true;
                        }

                    } else {
                        if (isTemporal) {
                            datosPDF.add(texto);
                        } else {
                            if (arrayListFormulario.get(i).isEsSubtitulo()) {
                                datosPDF.add("      " + titulo + " : " + texto);
                            } else {
                                datosPDF.add(titulo + " : " + texto);
                            }
                        }
                    }

                    break;
                case 2:
                    TimePicker timePickerFormularios = (TimePicker) elemento
                            .findViewById(R.id.timePickerFormularios);

                    String time = timePickerFormularios.getCurrentHour() + ":"
                            + timePickerFormularios.getCurrentMinute();

                    if (isTemporal) {
                        datosPDF.add(time);
                    } else {
                        datosPDF.add(titulo + " : " + time);
                    }

                    break;
                case 3:
                    CheckBox checkBoxFormularios = (CheckBox) elemento
                            .findViewById(R.id.checkBoxFormularios);

                    EditText editTextFormulariosCheckbox = (EditText) elemento
                            .findViewById(R.id.editTextFormulariosCheckbox);

                    if (checkBoxFormularios.isChecked()) {

                        if (isTemporal) {
                            datosPDF.add("No aplica");
                        } else {
                            if (arrayListFormulario.get(i).isEsSubtitulo()) {
                                datosPDF.add("      " + titulo + ": No aplica");
                            } else {
                                datosPDF.add(titulo + ": No aplica");
                            }

                        }
                    } else {
                        String textoEdit = editTextFormulariosCheckbox.getText()
                                .toString().trim();
                        if (textoEdit.equals("")) {
                            if (isTemporal) {
                                datosPDF.add("EMPTY");
                            } else {
                                return true;
                            }
                        } else {
                            if (isTemporal) {
                                datosPDF.add(textoEdit);
                            } else {
                                //datosPDF.add(titulo + " : " + textoEdit);

                                if (arrayListFormulario.get(i).isEsSubtitulo()) {
                                    datosPDF.add("      " + titulo + " : " + textoEdit);
                                } else {
                                    datosPDF.add(titulo + " : " + textoEdit);
                                }

                            }
                        }
                    }
                    break;
                case 4:

                    Spinner spinnerFormularios = (Spinner) elemento
                            .findViewById(R.id.spinnerFormularios);

                    if (isTemporal) {
                        datosPDF.add(spinnerFormularios.getSelectedItem()
                                .toString());
                    } else {
                        if (arrayListFormulario.get(i).isEsSubtitulo()) {
                            datosPDF.add("      " + titulo + " : " + spinnerFormularios.getSelectedItem().toString());
                        } else {
                            datosPDF.add(titulo + " : " + spinnerFormularios.getSelectedItem().toString());
                        }
                    }

                    break;
                case 5:
                    DatePicker datePickerFecha = (DatePicker) elemento
                            .findViewById(R.id.datePickerFecha);

                    int day = datePickerFecha.getDayOfMonth();
                    int month = datePickerFecha.getMonth();
                    int year = datePickerFecha.getYear();

                    Calendar cal = Calendar.getInstance();
                    cal.set(year, month, day);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String formatedDate = sdf.format(cal.getTime());

                    if (isTemporal) {
                        datosPDF.add(formatedDate);
                    } else {
                        datosPDF.add(titulo + " : " + formatedDate);
                    }
                    break;
                default:
                    datosPDF.add(titulo);

                    break;
            }
        }
        for (int i = 0; i < datosPDF.size(); i++) {
            Log.w("Datos :  ", datosPDF.get(i));
        }
        return false;
    }

    public static class TabListener<T extends Fragment> implements
            ActionBar.TabListener {
        private Fragment mFragment;
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;
        private final Bundle mArgs;

        /**
         * Constructor used each time a new tab is created.
         *
         * @param activity The host Activity, used to instantiate the fragment
         * @param tag      The identifier tag for the fragment
         * @param clz      The fragment's Class, used to instantiate the fragment
         */

        public TabListener(Activity activity, String tag, Class<T> clz) {
            this(activity, tag, clz, null);
        }

        public TabListener(Activity activity, String tag, Class<T> clz,
                           Bundle args) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
            mArgs = args;

            // Check to see if we already have a fragment for this tab, probably
            // from a previously saved state. If so, deactivate it, because our
            // initial state is that a tab isn't shown.
            mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);
            if (mFragment != null && !mFragment.isDetached()) {
                FragmentTransaction ft = mActivity.getFragmentManager()
                        .beginTransaction();
                ft.detach(mFragment);
                ft.commit();
            }
        }

        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            if (mFragment == null) {
                mFragment = Fragment.instantiate(mActivity, mClass.getName(),
                        mArgs);
                ft.add(android.R.id.content, mFragment, mTag);
            } else {
                ft.show(mFragment);
            }
        }

        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            if (mFragment != null) {
                ft.hide(mFragment);
            }
        }

        public void onTabReselected(Tab tab, FragmentTransaction ft) {
        }

    }

    public static class ExampleFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            pic1 = null;
            pic2 = null;
            isFoto1Default = true;
            isFoto2Default = true;
            FormularioFactory mFormularioFactory = new FormularioFactory();

            int numeroFormulario = getActivity().getIntent().getIntExtra(
                    "numeroFormulario", 1);

            int reps = getActivity().getIntent().getIntExtra("reps", 1);

            arrayListFormulario = mFormularioFactory.getFormulario(
                    numeroFormulario, reps);

            // Inflate the layout for this fragment
            View rootView = inflater.inflate(R.layout.activity_formulario,
                    container, false);

            LinearLayout formulario = (LinearLayout) rootView
                    .findViewById(R.id.linearLayoutFormulario);

            FormularioViewGenerator mFormularioViewGenerator = new FormularioViewGenerator(
                    inflater, container, getActivity(), arrayListFormulario);

            arrayListElementosFormulario = mFormularioViewGenerator
                    .generarFormulario();

            for (int i = 0; i < arrayListElementosFormulario.size(); i++) {
                formulario.addView(arrayListElementosFormulario.get(i),
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
            }

            if (esCargar) {
                cargarDatos(form);
            }

            return rootView;
        }

        @SuppressWarnings("unchecked")
        private void cargarDatos(ArrayList<String> form) {
            for (int i = 0; i < arrayListFormulario.size(); i++) {

                View elemento = arrayListElementosFormulario.get(i);

                switch (arrayListFormulario.get(i).getTipo()) {
                    case 1:
                        EditText editTextFormularios = (EditText) elemento
                                .findViewById(R.id.editTextFormularios);

                        if (!form.get(i + 3).equals("EMPTY")) {
                            editTextFormularios.setText(form.get(i + 3));
                        }
                        break;
                    case 2:
                        TimePicker timePickerFormularios = (TimePicker) elemento
                                .findViewById(R.id.timePickerFormularios);

                        String[] time = form.get(i + 3).split(":");
                        timePickerFormularios.setCurrentHour(Integer
                                .parseInt(time[0]));
                        timePickerFormularios.setCurrentMinute(Integer
                                .parseInt(time[1]));

                        break;
                    case 3:
                        CheckBox checkBoxFormularios = (CheckBox) elemento
                                .findViewById(R.id.checkBoxFormularios);

                        EditText editTextFormulariosCheckbox = (EditText) elemento
                                .findViewById(R.id.editTextFormulariosCheckbox);

                        if (form.get(i + 3).equals("No aplica")) {
                            checkBoxFormularios.setChecked(true);
                        } else {
                            checkBoxFormularios.setChecked(false);
                            if (!form.get(i + 3).equals("EMPTY")) {
                                editTextFormulariosCheckbox
                                        .setText(form.get(i + 3));
                            }
                        }

                        break;
                    case 4:

                        Spinner spinnerFormularios = (Spinner) elemento
                                .findViewById(R.id.spinnerFormularios);

                        ArrayAdapter<String> myAdap = (ArrayAdapter<String>) spinnerFormularios
                                .getAdapter();
                        int spinnerPosition = myAdap.getPosition(form.get(i + 3));

                        // set the default according to value
                        spinnerFormularios.setSelection(spinnerPosition);

                        break;
                    case 5:
                        DatePicker datePickerFecha = (DatePicker) elemento
                                .findViewById(R.id.datePickerFecha);

                        String[] date = form.get(i + 3).split("-");

                        int day = Integer.parseInt(date[0]);
                        int month = Integer.parseInt(date[1]) - 1;
                        int year = Integer.parseInt(date[2]);

                        datePickerFecha.updateDate(year, month, day);
                        break;
                    default:
                        break;
                }
            }

        }
    }


    public static class ExampleFragmentTwo extends Fragment {

        private static final int MAX_IMAGE_DIMENSION = 250;
        private Spinner spinnerObservaciones;
        public View lastContextMenuButton;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment

            View rootView = inflater.inflate(R.layout.activity_observaciones,
                    container, false);
            inicializarSpinner(rootView);
            inicializarObjetos(rootView);
            pic1 = null;
            pic2 = null;
            isFoto1Default = true;
            isFoto2Default = true;
            evidenciaEscrita.setText("");

            if (esCargar) {
                evidenciaEscritaCargar = form.get(form.size() - 3);
                pic1Cargar = form.get(form.size() - 2);
                pic2Cargar = form.get(form.size() - 1);

                if (!pic1Cargar.equals("--1")) {
                    pic1 = decodeBase64(pic1Cargar);

                    fotoBitmapFinal = Bitmap.createScaledBitmap(pic1, 400, 400,
                            false);

                    foto1.setImageBitmap(fotoBitmapFinal);

                    isFoto1Default = false;
                }
                if (!pic2Cargar.equals("--2")) {
                    pic2 = decodeBase64(pic2Cargar);

                    fotoBitmapFinal = Bitmap.createScaledBitmap(pic2, 400, 400,
                            false);

                    foto2.setImageBitmap(fotoBitmapFinal);
                    isFoto2Default = false;
                }
                if (!evidenciaEscritaCargar.equals("EMPTY")) {
                    evidenciaEscrita.setText(evidenciaEscritaCargar
                            .substring(19));
                }

            }

            return rootView;
        }

        private void inicializarSpinner(View rootView) {
            ArrayList<String> SpinnerArray = new ArrayList<String>();
            SpinnerArray.add("Evidencia fotográfica");
            SpinnerArray.add("Evidencia escrita");

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getActivity(), android.R.layout.simple_spinner_item,
                    SpinnerArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerObservaciones = (Spinner) rootView
                    .findViewById(R.id.spinnerObservaciones);
            spinnerObservaciones.setAdapter(adapter);
            spinnerObservaciones.setPrompt("Seleccione opcion");
            spinnerObservaciones
                    .setOnItemSelectedListener(new OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int posicion, long arg3) {

                            switch (posicion) {
                                case 0:
                                    evidenciaEscrita.setVisibility(View.GONE);
                                    foto1.setVisibility(View.VISIBLE);
                                    foto2.setVisibility(View.VISIBLE);
                                    break;

                                case 1:
                                    foto1.setVisibility(View.INVISIBLE);
                                    foto2.setVisibility(View.INVISIBLE);
                                    evidenciaEscrita.setVisibility(View.VISIBLE);
                                    break;

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }

                    });

        }

        private void inicializarObjetos(View rootView) {
            foto1 = (ImageView) rootView
                    .findViewById(R.id.imageViewObservaciones1);
            foto2 = (ImageView) rootView
                    .findViewById(R.id.imageViewObservaciones2);
            evidenciaEscrita = (EditText) rootView
                    .findViewById(R.id.editTextObservaciones);
            evidenciaEscrita.setText("");
            registerForContextMenu(foto1);
            registerForContextMenu(foto2);
        }

        //Space for tryout

        private void setPic(ImageView mImageView) {
            // Get the dimensions of the View
            int targetW = mImageView.getWidth();
            int targetH = mImageView.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);


            ///Bitmap rotation try

            int rotate = 0;
            try {
                File imageFile = new File(mCurrentPhotoPath);
                ExifInterface exif = new ExifInterface(
                        imageFile.getAbsolutePath());
                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);

                Log.e("Orientation", " " + orientation);

                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotate = 270;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotate = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotate = 90;
                        break;
                }
                Log.e("Torcida", "Exif orientation: " + orientation);
                /****** Image rotation ****/
                Matrix matrix = new Matrix();
                matrix.setRotate(rotate, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
                //matrix.postRotate(orientation);
                Bitmap cropped = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                mImageView.setImageBitmap(cropped);
            } catch (Exception e) {
                e.printStackTrace();
            }


            //Put Rotated
            //mImageView.setImageBitmap(bitmap);
        }

        private Bitmap improvePic() {
            // Get the dimensions of the View
            int targetW = 400;
            int targetH = 400;

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;

            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

            Bitmap cropped = bitmap;
            ///Bitmap rotation try

            int rotate = 0;
            try {
                File imageFile = new File(mCurrentPhotoPath);
                ExifInterface exif = new ExifInterface(
                        imageFile.getAbsolutePath());
                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);

                Log.e("Orientation", " " + orientation);

                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotate = 270;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotate = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotate = 90;
                        break;
                }
                Log.e("Torcida", "Exif orientation: " + orientation);
                /****** Image rotation ****/
                Matrix matrix = new Matrix();
                matrix.setRotate(rotate, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
                //matrix.postRotate(orientation);
                cropped = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return cropped;


        }


        public void onActivityResult(int requestCode, int resultCode,
                                     Intent imageReturnedIntent) {
            super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
            Bundle extras = null;
            if (imageReturnedIntent != null) {
                extras = imageReturnedIntent.getExtras();
            }

            switch (requestCode) {

                //case 0:// Toma foto
                case CAMERA_REQUEST:// Toma foto
                    if (resultCode == RESULT_OK) {
                        // fotoBitmapFinal = (Bitmap)
                        // imageReturnedIntent.getExtras().get("data");

                        //Log.e("Pic", "" + (extras == null));

                        /*byte[] data = extras.getByteArray("pic");

                        fotoBitmapFinal = BitmapFactory.decodeByteArray(data, 0,
                                data.length);

                        pic1 = redimensionarImagen(fotoBitmapFinal);
                        fotoBitmapFinal = Bitmap.createScaledBitmap(
                                fotoBitmapFinal, 400, 400, false);

                        foto1.setImageBitmap(fotoBitmapFinal);

                        isFoto1Default = false;*/

                        File file = new File(mCurrentPhotoPath);
                        if (file.exists()) {
                            Log.e("File", "exists");
                            setPic(foto1);
                            pic1 = redimensionarImagen(improvePic());
                            isFoto1Default = false;
                            Log.e("File", file.delete() + " noes exists");
                        } else {
                            Log.e("File", "oh noes exists");

                        }


                    }

                    break;
                case 1:// Sube foto
                    if (resultCode == RESULT_OK) {
                        Uri selectedImage = imageReturnedIntent.getData();

					/*
                     * try { fotoBitmapFinal = getCorrectlyOrientedImage(
					 * getActivity(), selectedImage); } catch (IOException e) {
					 * e.printStackTrace(); }
					 */

                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getActivity().getContentResolver().query(
                                selectedImage, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();
                        fotoBitmapFinal = BitmapFactory.decodeFile(picturePath);
                        pic1 = redimensionarImagen(fotoBitmapFinal);
                        fotoBitmapFinal = Bitmap.createScaledBitmap(
                                fotoBitmapFinal, 400, 400, false);

                        foto1.setImageBitmap(fotoBitmapFinal);

                        isFoto1Default = false;
                    }
                    break;
                case CAMERA_REQUEST_2:// Toma foto
                    if (resultCode == RESULT_OK) {

                        /*byte[] data = extras.getByteArray("pic");

                        fotoBitmapFinal = BitmapFactory.decodeByteArray(data, 0,
                                data.length);

                        pic2 = redimensionarImagen(fotoBitmapFinal);
                        fotoBitmapFinal = Bitmap.createScaledBitmap(
                                fotoBitmapFinal, 400, 400, false);

                        foto2.setImageBitmap(fotoBitmapFinal);
                        isFoto2Default = false;*/


                        File file = new File(mCurrentPhotoPath);
                        if (file.exists()) {
                            Log.e("File 2", "exists");
                            setPic(foto2);
                            pic2 = redimensionarImagen(improvePic());
                            isFoto2Default = false;
                            Log.e("File 2", file.delete() + " noes exists");
                        } else {
                            Log.e("File 2", "oh noes exists");

                        }


                    }

                    break;
                case 3:// Sube foto
                    if (resultCode == RESULT_OK) {
                        Uri selectedImage = imageReturnedIntent.getData();

                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getActivity().getContentResolver().query(
                                selectedImage, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();
                        fotoBitmapFinal = BitmapFactory.decodeFile(picturePath);

                        pic2 = redimensionarImagen(fotoBitmapFinal);
                        fotoBitmapFinal = Bitmap.createScaledBitmap(
                                fotoBitmapFinal, 400, 400, false);

                        foto2.setImageBitmap(fotoBitmapFinal);
                        isFoto2Default = false;

                    }
                    break;
                default:
                    break;
            }
        }

        private Bitmap redimensionarImagen(Bitmap fotoBitmapFinal) {

            Bitmap result = null;
            int height = fotoBitmapFinal.getHeight();
            int width = fotoBitmapFinal.getWidth();
            if (height > width) {
                int widthFinal = (int) Math.floor((width * 800) / height);
                result = Bitmap.createScaledBitmap(fotoBitmapFinal, widthFinal,
                        800, false);
            } else if (width > height) {
                int heightFinal = (int) Math.floor((height * 800) / width);
                result = Bitmap.createScaledBitmap(fotoBitmapFinal, 800,
                        heightFinal, false);
            } else {
                result = Bitmap.createScaledBitmap(fotoBitmapFinal, 800, 800,
                        false);
            }

            return result;
        }

        public static int getOrientation(Context context, Uri photoUri) {
            /* it's on the external media. */
            Cursor cursor = context
                    .getContentResolver()
                    .query(photoUri,
                            new String[]{MediaStore.Images.ImageColumns.ORIENTATION},
                            null, null, null);

            if (cursor.getCount() != 1) {
                return -1;
            }

            cursor.moveToFirst();
            return cursor.getInt(0);
        }

        public static Bitmap getCorrectlyOrientedImage(Context context,
                                                       Uri photoUri) throws IOException {
            InputStream is = context.getContentResolver().openInputStream(
                    photoUri);
            BitmapFactory.Options dbo = new BitmapFactory.Options();
            dbo.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, dbo);
            is.close();

            int rotatedWidth, rotatedHeight;
            int orientation = getOrientation(context, photoUri);

            if (orientation == 90 || orientation == 270) {
                rotatedWidth = dbo.outHeight;
                rotatedHeight = dbo.outWidth;
            } else {
                rotatedWidth = dbo.outWidth;
                rotatedHeight = dbo.outHeight;
            }

            Bitmap srcBitmap;
            is = context.getContentResolver().openInputStream(photoUri);
            if (rotatedWidth > MAX_IMAGE_DIMENSION
                    || rotatedHeight > MAX_IMAGE_DIMENSION) {
                float widthRatio = ((float) rotatedWidth)
                        / ((float) MAX_IMAGE_DIMENSION);
                float heightRatio = ((float) rotatedHeight)
                        / ((float) MAX_IMAGE_DIMENSION);
                float maxRatio = Math.max(widthRatio, heightRatio);

                // Create the bitmap from file
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = (int) maxRatio;
                srcBitmap = BitmapFactory.decodeStream(is, null, options);
            } else {
                srcBitmap = BitmapFactory.decodeStream(is);
            }
            is.close();

			/*
             * if the orientation is not 0 (or -1, which means we don't know),
			 * we have to do a rotation.
			 */
            if (orientation > 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate(orientation);

                srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0,
                        srcBitmap.getWidth(), srcBitmap.getHeight(), matrix,
                        true);
            }

            return srcBitmap;
        }

        // Creating Context Menu
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenuInfo menuInfo) {
            super.onCreateContextMenu(menu, v, menuInfo);
            lastContextMenuButton = v;
            menu.setHeaderTitle("Opciones de Fotos");
            menu.add(0, 0, 0, "Tomar Foto");
            menu.add(0, 1, 0, "Subir Foto");
            menu.add(0, 2, 0, "Eliminar");

        }

        public boolean onContextItemSelected(MenuItem item) {
            // find out which menu item was pressed

            switch (item.getItemId()) {
                case 0:
                    tomarFoto(lastContextMenuButton);
                    Log.v("Opcion", "Tomar foto");
                    return true;
                case 1:
                    subirFoto(lastContextMenuButton);
                    Log.v("Opcion", "Subir foto");
                    return true;
                case 2:
                    eliminarFoto(lastContextMenuButton);
                    Log.v("Opcion", "Eliminar");
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.main, menu);
        }

        //Test Method
        private void dispatchTakePictureIntent(int request_code) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Log.e("IO - Ex", ex.toString());
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                    startActivityForResult(takePictureIntent, request_code);
                }
            }
        }


        private File createImageFile() throws IOException {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            //File storageDir = Environment.getExternalStorageDirectory();
            /*File image = File.createTempFile(
                    imageFileName,  *//* prefix *//*
                    ".jpg",         *//* suffix *//*
                    storageDir      *//* directory *//*
            );*/

            File image = new File(Environment.getExternalStorageDirectory() + "/"
                    + imageFileName + ".jpg");

            // Save a file: path for use with ACTION_VIEW intents
            //mCurrentPhotoPath = "file:" + image.getAbsolutePath();
            mCurrentPhotoPath = image.getPath();
            Log.e("mCurrentPhotoPath", mCurrentPhotoPath);
            return image;
        }

        //Space for tryout

        private void tomarFoto(View v) {
            if (v == foto1) {

                //Intent intent = new Intent(getActivity(), CameraActivity.class);
                //startActivityForResult(intent, 0);
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);

                dispatchTakePictureIntent(CAMERA_REQUEST);

            } else if (v == foto2) {

                /*Intent intent = new Intent(getActivity(), CameraActivity.class);
                startActivityForResult(intent, 2);*/
                dispatchTakePictureIntent(CAMERA_REQUEST_2);

            }
        }

        private void subirFoto(View v) {
            if (v == foto1) {
                Intent pickPhoto = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);// one can be replced with
                // any action code
            } else if (v == foto2) {
                Intent pickPhoto = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 3);// one can be replced with
                // any action code
            }
        }

        private void eliminarFoto(View v) {
            if (v == foto1) {
                foto1.setImageResource(R.drawable.ic_launcher);
                isFoto1Default = true;
            } else if (v == foto2) {
                foto2.setImageResource(R.drawable.ic_launcher);
                isFoto2Default = true;
            }
        }

    }

    @Override
    public void onBackPressed() {
        prevenirPerdidaDatos();

    }

    private void prevenirPerdidaDatos() {
        // Recoger Encabezado
        datosPDF = new ArrayList<String>();
        String nombreFormulario = generalIntent
                .getStringExtra("nombreFormulario");

        datosPDF.add(nombreFormulario);
        datosPDF.add(now());

        int numeroFormulario = generalIntent.getIntExtra("numeroFormulario", 1);
        datosPDF.add("" + numeroFormulario);

        // Recoger datos

        if (recogerDatos(false)) {
            showMessageIncompleto();
        } else {
            datosPDF.add("Completo");
            obtenerEvidencia();
            showMessageDescartar();
        }
    }

    private void showMessageDescartar() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Descartar");
        alert.setMessage(getString(R.string.desea_descartarlo));

        alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alert.show();

    }


}
