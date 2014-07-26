package br.ufrpe.logrecife;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import br.ufrpe.logrecife.adapter.ListPickerAdapter;
import br.ufrpe.logrecife.model.Item;
import br.ufrpe.logrecife.model.LogRecife;

public class DialogPickerFragment extends DialogFragment {
	public LogRecife singleton;
	
	public interface OnOKDialogPickerFragmentListener{
		void onOK();
	}
	
	public static DialogPickerFragment newInstance(){
		return new DialogPickerFragment();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		singleton = LogRecife.get(this.getActivity());
		
		View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_picker, null);
		
		ListView listView = (ListView) v.findViewById(R.id.dialog_picker_list);
		
		ArrayList<Item> array = LogRecife.get(this.getActivity()).getLogradouro().getItems();
		//listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		listView.setAdapter(new ListPickerAdapter(this.getActivity(), array));
		
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				boolean  value = singleton.getLogradouro().getItems().get(position).isUserValue();
				singleton.getLogradouro().getItems().get(position).setUserValue(!value);
				CheckBox cb = (CheckBox) view.findViewById(R.id.list_picker_checkbox);
				cb.setChecked(!value);
			}
			
		});
		
		return new AlertDialog.Builder(getActivity())
        .setView(v)
        .setTitle("Confirme")
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //retornar com intent para fragmento e iniciar outra activity no fragmento.
            	OnOKDialogPickerFragmentListener activityListener = (OnOKDialogPickerFragmentListener) getActivity();
            	activityListener.onOK();
            }
        })
        .create();
	}
	
	

}
