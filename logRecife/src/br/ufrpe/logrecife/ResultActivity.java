package br.ufrpe.logrecife;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import br.ufrpe.logrecife.DialogPickerFragment.OnOKDialogPickerFragmentListener;
import br.ufrpe.logrecife.model.Item;
import br.ufrpe.logrecife.model.LogRecife;
import br.ufrpe.logrecife.model.Logradouro;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class ResultActivity extends FragmentActivity implements OnOKDialogPickerFragmentListener {

	LatLng latLng;
	GoogleMap map;
	SupportMapFragment mMapFragment;
	Logradouro logradouro;
	private static final String DIALOG_PICKER = "picker";
	private static final int REQUEST_PICKER = 0;

	public ResultActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.fragment_result2);



		//map = ((MapFragment) this.getFragmentManager().findFragmentById(R.id.map)).getMap();
		//ArrayList<String> array = this.getIntent().getExtras().getStringArrayList("list");
		logradouro = LogRecife.get(this).getLogradouro();
		ArrayList<Item> arrayItem = logradouro.getItems();
		ArrayList<String> array = new ArrayList<String>();
		
		
		for(Item i:arrayItem){
			array.add(i.toString()); 
		}

		mMapFragment = SupportMapFragment.newInstance();
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.container1, mMapFragment, "map");



		
		ListFragment list = new ListFragment();
		list.setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, array));
		fragmentTransaction.add(R.id.container2, list, "list");
		fragmentTransaction.commit();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		//latLng = intent.getParcelableExtra("latlng");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		map = mMapFragment.getMap();


		PolylineOptions rectOptions = new PolylineOptions()
		.add(new LatLng(-8.063414, -34.882459))
		.add(new LatLng(-8.063477, -34.882488))  
		.add(new LatLng(-8.064521, -34.878642))  
		.add(new LatLng(-8.064415, -34.878631))  
		.add(new LatLng(-8.063414, -34.882459)); 

		Polyline polyline = map.addPolyline(rectOptions);
		polyline.setWidth(2);
		//map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-8.064099, -34.880168), 16.5f), 2000, null);

		latLng = logradouro.getLatLng();

		if (latLng != null){
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.5f), 2000, null);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		this.getMenuInflater().inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()) {

		case R.id.item1:
			onClick();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onClick(){
		
		Intent i = new Intent(this, ReportActivity.class);
		this.startActivity(i);
		
		//DialogPickerFragment dialog = new DialogPickerFragment();
		//dialog.show(this.getSupportFragmentManager(), DIALOG_PICKER);
	}

	@Override
	public void onOK() {
		Intent i = new Intent(this, ReportActivity.class);
		this.startActivity(i);
		
	}



}
