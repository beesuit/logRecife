package br.ufrpe.logrecife.task;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.AsyncTask;
import android.util.Log;
import br.ufrpe.logrecife.ResultActivity;
import br.ufrpe.logrecife.model.Item;
import br.ufrpe.logrecife.model.LogRecife;
import br.ufrpe.logrecife.model.Logradouro;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

public class RequestTask extends AsyncTask<Void, Void, Void> {
	protected Context context;
	static RequestTask sTask;
	protected Address mLookUpAddress;
	protected ArrayList<Item> list;
	protected LatLng latLng;

	protected ProgressDialog atualizando;

	public RequestTask(Context c, Address address){
		context = c;
		mLookUpAddress = address;
		atualizando = new ProgressDialog(context);
	}

	public static RequestTask getInstance(Context c, Address address){
		sTask = new RequestTask(c, address);
		return sTask;
	}

	@Override
	protected void onPreExecute(){
		atualizando.setMessage("Procurando...");
		atualizando.setCancelable(false);
		atualizando.show();

	}

	@Override
	protected Void doInBackground(Void... params) {
		
		list = new ArrayList<Item>();
		
		for (int i = 0; i < 11; i++){
			
			list.add(new Item("Item "+ i, true));
		}
		
		latLng = prepareLatLng(mLookUpAddress);
		
		//TODO
		String gson = new Gson().toJson(mLookUpAddress);
		Log.e("hello", gson);

		Logradouro logradouro = new Logradouro(mLookUpAddress, latLng, list);
		LogRecife singleton = LogRecife.get(context);
		
		singleton.setLogradouro(logradouro);
		
		//http request para o server
		

		return null;

	}

	@Override
	protected void onPostExecute(Void object) {
		atualizando.dismiss();
		//Toast.makeText(context, teste, Toast.LENGTH_LONG).show();
		
		Intent i = new Intent(context, ResultActivity.class);
		//Bundle bundle = new Bundle();
		//bundle.putStringArrayList("list", list);
		//bundle.putParcelable("latLng", latLng);
		//i.putExtras(bundle);
		context.startActivity(i);
	}
	
	public LatLng prepareLatLng(Address address){
		if(address.hasLatitude() && address.hasLongitude()){
			return new LatLng(address.getLatitude(), address.getLongitude());
		}else{
			return null;
		}

	}

}
