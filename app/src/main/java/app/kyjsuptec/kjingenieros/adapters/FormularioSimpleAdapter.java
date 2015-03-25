package app.kyjsuptec.kjingenieros.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import app.kyjsuptec.kjingenieros.R;

public class FormularioSimpleAdapter extends ArrayAdapter<String> {
	private Context context;
	private ArrayList<String> values;

	public FormularioSimpleAdapter(Context context, ArrayList<String> values) {
		super(context, R.layout.row_layout_simple_formulario, values);
		this.values = values;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.row_layout_simple_formulario,
				parent, false);

		TextView txtViewMenuPpalOpcion = (TextView) rowView
				.findViewById(R.id.txtViewMenuPpalOpcion);

		txtViewMenuPpalOpcion.setText(values.get(position));

		return rowView;
	}

}
