package br.ufrpe.logrecife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import br.ufrpe.logrecife.adapter.ReportListAdapter;
import br.ufrpe.logrecife.model.LogRecife;

public class ReportListFragment extends ListFragment {
	public static final String UUID = "uuid";
	private LogRecife singleton; 
	private ReportListAdapter adapter;
	

	public static ReportListFragment newInstance(){
		ReportListFragment fragment = new ReportListFragment();
		return fragment;
	}
	
	public ReportListFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		singleton = LogRecife.get(this.getActivity());
		
		adapter = new ReportListAdapter(getActivity(), singleton.getReports());
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent i = new Intent(getActivity(), ReportActivity.class);
		Log.e("teste", singleton.getReports().get(position).getUuid().toString());
		i.putExtra(UUID,singleton.getReports().get(position).getUuid());
		startActivity(i);
		
	}

}
