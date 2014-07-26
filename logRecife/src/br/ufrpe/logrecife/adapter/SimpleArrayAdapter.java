package br.ufrpe.logrecife.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import br.ufrpe.logrecife.R;

public class SimpleArrayAdapter extends ArrayAdapter<String>{

	private final Context context;
	private final ArrayList<String> array;

	public SimpleArrayAdapter(Context context, ArrayList<String> array) {
		super(context, R.layout.search_list_row, array);
		this.context = context;
		this.array = array;
		

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.simple_item, parent, false);
		String i = array.get(position);


		TextView teste = (TextView) rowView.findViewById(R.id.textView1);


		teste.setText(i);


		return rowView;
	}

}