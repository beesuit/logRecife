package br.ufrpe.logrecife;

import java.util.ArrayList;

import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import br.ufrpe.logrecife.adapter.MyArrayAdapter;
import br.ufrpe.logrecife.task.RequestTask;

import com.google.android.gms.maps.model.LatLng;

public class SearchListFragment extends ListFragment {
	
	ArrayList<Address> array = new ArrayList<Address>();

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setRetainInstance(true);
		array = this.getArguments().getParcelableArrayList("teste");
		/*ArrayList<String> array = new ArrayList<String>();

		for(int i = 0; i<21; i++){
			array.add("Item"+i);
		}*/
		
		MyArrayAdapter adapter = new MyArrayAdapter(this.getActivity(), array);
		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), 
			//	android.R.layout.simple_list_item_1, android.R.id.text1, array);
		
		setListAdapter(adapter);

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
		RequestTask task = RequestTask.getInstance(this.getActivity(), array.get(position));
		task.execute();
		
		//FragmentManager fm = this.getActivity().getSupportFragmentManager();
		//fm.beginTransaction().replace(R.id.container, newFragment).commit();
		
	}
	
	public LatLng prepareExtra(Address address){
		if(address.hasLatitude() && address.hasLongitude()){
			return new LatLng(address.getLatitude(), address.getLongitude());
		}else{
			return null;
		}

	}
	
}
