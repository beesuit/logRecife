package br.ufrpe.logrecife;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapDialogFragment extends DialogFragment {

	public static final String EXTRA_LAT_LNG = "latLng";
	public static final String EXTRA_POLYLINE = "polyline";
	private SupportMapFragment mapFragment;
	private LatLng latLng;

	public static MapDialogFragment newInstance(LatLng latLng){
		Bundle args = new Bundle();
		args.putParcelable(EXTRA_LAT_LNG, latLng);

		MapDialogFragment fragment = new MapDialogFragment();
		fragment.setArguments(args);
		fragment.setStyle(STYLE_NO_TITLE, 0);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		latLng = (LatLng) getArguments().getParcelable(EXTRA_LAT_LNG);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.activity_single_fragment, container, false);
		
		mapFragment = SupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction().add(R.id.single_container, mapFragment).commit();
        
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		GoogleMap map = mapFragment.getMap();
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.5f), 2000, null);

	}



}
