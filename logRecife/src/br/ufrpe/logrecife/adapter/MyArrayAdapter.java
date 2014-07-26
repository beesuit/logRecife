package br.ufrpe.logrecife.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import br.ufrpe.logrecife.R;

public class MyArrayAdapter extends ArrayAdapter<Address>{

	private final Context context;
	private final ArrayList<Address> array;

	public MyArrayAdapter(Context context, ArrayList<Address> array) {
		super(context, R.layout.search_list_row, array);
		this.context = context;
		this.array = array;
		

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.search_list_row, parent, false);
		Address i = array.get(position);


		TextView teste = (TextView) rowView.findViewById(R.id.textView1);


		teste.setText(i.getAddressLine(0));


		return rowView;
	}

}
