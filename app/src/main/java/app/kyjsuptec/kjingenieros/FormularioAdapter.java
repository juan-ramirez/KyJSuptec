package app.kyjsuptec.kjingenieros;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class FormularioAdapter extends ArrayAdapter<String> {
	private Context context;
	private ArrayList<String> values;
	private Boolean[] checked;

	public FormularioAdapter(Context context, ArrayList<String> values) {
		super(context, R.layout.row_layout_formularios, values);
		this.values = values;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.row_layout_formularios,
				parent, false);

		checked = new Boolean[values.size()];
		Arrays.fill(checked, false);
		final int pos = position;

		CheckBox checkBoxOpcion = (CheckBox) rowView
				.findViewById(R.id.checkBoxOpcion);

		checkBoxOpcion
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						checked[pos] = isChecked;

					}
				});

		TextView txtViewMenuPpalOpcion = (TextView) rowView
				.findViewById(R.id.txtViewMenuPpalOpcion);

		txtViewMenuPpalOpcion.setText(values.get(position));

		return rowView;
	}

	public Boolean[] getChecked() {
		return checked;
	}

}
