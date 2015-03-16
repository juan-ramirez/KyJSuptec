package app.kyjsuptec.kjingenieros;

import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;

public class FormularioViewGenerator {

    LayoutInflater inflater;
    ViewGroup container;
    Context context;
    ArrayList<DatoFormularioFactory> arrayListFormulario;

    public FormularioViewGenerator(LayoutInflater inflater,
                                   ViewGroup container, Context context,
                                   ArrayList<DatoFormularioFactory> arrayListFormulario) {
        this.inflater = inflater;
        this.container = container;
        this.context = context;
        this.arrayListFormulario = arrayListFormulario;
    }

    public ArrayList<View> generarFormulario() {
        ArrayList<View> arrayListElementosFormulario = new ArrayList<View>();

        for (int i = 0; i < arrayListFormulario.size(); i++) {
            arrayListElementosFormulario.add(getView(i));
        }

        return arrayListElementosFormulario;
    }

    public View getView(int position) {

        View rowView = null;

        switch (arrayListFormulario.get(position).getTipo()) {
            case 1:
                rowView = inflater.inflate(R.layout.row_layout_formulario_1,
                        container, false);
                EditText editTextFormularios = (EditText) rowView
                        .findViewById(R.id.editTextFormularios);
                editTextFormularios.setSingleLine();

                switch (arrayListFormulario.get(position).getTipoDato()) {
                    case 1:
                        editTextFormularios.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case 3:

                        editTextFormularios
                                .setRawInputType(InputType.TYPE_CLASS_NUMBER);
                        break;
                    case 4:
                        editTextFormularios.setRawInputType(InputType.TYPE_CLASS_NUMBER
                                | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        break;
                }
                break;
            case 2:
                rowView = inflater.inflate(R.layout.row_layout_formulario_2,
                        container, false);
                TimePicker timePickerFormularios = (TimePicker) rowView
                        .findViewById(R.id.timePickerFormularios);
                timePickerFormularios.setIs24HourView(true);

                break;
            case 3:
                rowView = inflater.inflate(R.layout.row_layout_formulario_3,
                        container, false);

                final EditText editTextFormulariosCheckbox = (EditText) rowView
                        .findViewById(R.id.editTextFormulariosCheckbox);
                editTextFormulariosCheckbox.setSingleLine();

                switch (arrayListFormulario.get(position).getTipoDato()) {
                    case 1:
                        editTextFormulariosCheckbox
                                .setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case 3:
                        editTextFormulariosCheckbox
                                .setRawInputType(InputType.TYPE_CLASS_NUMBER);
                        break;
                }

                CheckBox checkBoxFormularios = (CheckBox) rowView
                        .findViewById(R.id.checkBoxFormularios);

                checkBoxFormularios
                        .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(CompoundButton buttonView,
                                                         boolean isChecked) {
                                if (isChecked) {
                                    editTextFormulariosCheckbox.setEnabled(false);
                                } else {
                                    editTextFormulariosCheckbox.setEnabled(true);
                                }
                            }
                        });

                break;
            case 4:
                rowView = inflater.inflate(R.layout.row_layout_formulario_4,
                        container, false);
                Spinner spinnerFormularios = (Spinner) rowView
                        .findViewById(R.id.spinnerFormularios);
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context,R.layout.simple_dropdown_item ,
                        arrayListFormulario.get(position).getListaSpinner());
                spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_dropdown_item_spinner);
                spinnerFormularios.setAdapter(spinnerArrayAdapter);

                break;
            case 5:
                rowView = inflater.inflate(R.layout.row_layout_formulario_5,
                        container, false);
                break;

            default:
                rowView = inflater.inflate(R.layout.row_layout_formulario_6,
                        container, false);
                break;
        }

        TextView textViewFormularios = (TextView) rowView
                .findViewById(R.id.textViewFormularios);

        textViewFormularios.setText(arrayListFormulario.get(position)
                .getTitulo());

        return rowView;
    }

}
