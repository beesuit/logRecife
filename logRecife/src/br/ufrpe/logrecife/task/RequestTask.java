package br.ufrpe.logrecife.task;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

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
//TODO done
public class RequestTask extends AsyncTask<Void, Void, Void> {
	protected Context context;
	static RequestTask sTask;
	protected Address mLookUpAddress;
	protected ArrayList<Item> list;
	protected LatLng latLng;

	protected ProgressDialog searching;

	public RequestTask(Context c, Address address){
		context = c;
		mLookUpAddress = address;
		searching = new ProgressDialog(context);
	}

	public static RequestTask getInstance(Context c, Address address){
		sTask = new RequestTask(c, address);
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
		
		list = new ArrayList<Item>();
		
		for (int i = 0; i < 11; i++){
			
			list.add(new Item("Item "+ i, true));
		}
		
		latLng = prepareLatLng(mLookUpAddress);
		
		String gson = new Gson().toJson(mLookUpAddress);

		Logradouro logradouro = new Logradouro(mLookUpAddress, latLng, list);
		LogRecife singleton = LogRecife.get(context);
		
		singleton.setLogradouro(logradouro);
		
		if(false){
			HttpClient hc = new DefaultHttpClient();
	        String URL = "http://192.168.254.4:8080/Teste/Teste?lat:123";
	        HttpGet get = new HttpGet(URL);
	        HttpResponse rp;
			
	        try {
				rp = hc.execute(get);
				if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
	                String prefString = EntityUtils.toString(rp.getEntity());
	                //objects = new JSONArray(prefString);
	                Log.e("request", prefString);                
	                }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        

			return null;
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void object) {
		searching.dismiss();
		
		Intent i = new Intent(context, ResultActivity.class);
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
