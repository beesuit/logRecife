package br.ufrpe.logrecife;

import java.util.ArrayList;

import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import br.ufrpe.logrecife.adapter.MyArrayAdapter;
import br.ufrpe.logrecife.task.RequestTask;

public class SearchListFragment extends ListFragment {
	private final static String ADDRESS_LIST = "addressList";
	
	ArrayList<Address> addressList = new ArrayList<Address>();
	
	public static SearchListFragment newInstance(ArrayList<Address> addressList){
		Bundle args = new Bundle();
		args.putParcelableArrayList(ADDRESS_LIST, addressList);
		SearchListFragment fragment = new SearchListFragment();
		fragment.setArguments(args);
		
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setRetainInstance(true);
		addressList = this.getArguments().getParcelableArrayList(ADDRESS_LIST);
		
		MyArrayAdapter adapter = new MyArrayAdapter(this.getActivity(), addressList);
		setListAdapter(adapter);

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
		RequestTask task = RequestTask.getInstance(this.getActivity(), addressList.get(position));
		task.execute();
				
	}
	
}