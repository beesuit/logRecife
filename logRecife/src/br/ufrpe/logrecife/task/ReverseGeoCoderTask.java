package br.ufrpe.logrecife.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

//TODO done
public class ReverseGeoCoderTask extends AsyncTask<Void, Void, Void> {
	protected Context context;
	static ReverseGeoCoderTask sTask;
	protected FragmentManager fm;
	protected LatLng latLng;
	protected Address mResultAddress;
	protected ArrayList<Address> mListAddress = new ArrayList<Address>();

	protected ProgressDialog searching;

	public ReverseGeoCoderTask(Context c, LatLng latLng){
		context = c;
		this.latLng = latLng;
		searching = new ProgressDialog(context);
	}

	public static ReverseGeoCoderTask getInstance(Context c, LatLng latLng){
		sTask = new ReverseGeoCoderTask(c, latLng);
		return sTask;
	}

	@Override
	protected void onPreExecute(){
		searching.setMessage("Procurando...");
		searching.setCancelable(false);
		searching.show();

	}

	@Override
	protected Void doInBackground(Void... params) {
		Geocoder geocoder = new Geocoder(context);
		List<Address> addresses = null;

		try {
			addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 10);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(addresses != null && addresses.size() > 0){
			for(int i = 0; i < addresses.size(); i += 1){
				mListAddress.add(addresses.get(i));
			}
		}

		return null;

	}

	@Override
	protected void onPostExecute(Void object) {
		searching.dismiss();
		
		if (mListAddress.size() >= 1){
			RequestTask task = RequestTask.getInstance(context, mListAddress.get(0));
			task.execute();
		}else{
			Toast.makeText(context, "Não foi encontrado nenhum endereço", Toast.LENGTH_LONG).show();
		}
	}

}
