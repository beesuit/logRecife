package br.ufrpe.logrecife.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import br.ufrpe.logrecife.R;
import br.ufrpe.logrecife.model.Item;

public class ListPickerAdapter extends ArrayAdapter<Item> {
	
	private final Context context;
	private final ArrayList<Item> items;
	private final static int LAYOUT = R.layout.list_picker_item;
	
	public ListPickerAdapter(Context context, ArrayList<Item> items){
		super(context, LAYOUT, items);
		
		this.context = context;
		this.items = items;
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(LAYOUT, parent, false);
		
		TextView tv = (TextView) v.findViewById(R.id.list_picker_textview);
		
		tv.setText(items.get(position).toString());
		
		CheckBox cb = (CheckBox)v.findViewById(R.id.list_picker_checkbox);
		cb.setChecked(items.get(position).isUserValue());
		

		return v;
	}
	
}
