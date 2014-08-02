package br.ufrpe.logrecife.model;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class LogRecife {
	private static LogRecife sLogRecife;
	private Context mAppContext;
	private Logradouro logradouro;
	private Report report;
	private ArrayList<Report> reports;

	public LogRecife(Context context) {
		mAppContext = context;
		setReports(new ArrayList<Report>());
	}
	
	public static LogRecife get(Context context){
		if (sLogRecife == null){
			sLogRecife = new LogRecife(context.getApplicationContext());
		}
		return sLogRecife;
	}
	
	public Logradouro getLogradouro(){
		return logradouro;
	}
	
	public void setLogradouro(Logradouro logradouro){
		this.logradouro = logradouro;
	}
	
	public Report getReport(){
		return report;
	}
	
	public void setReport(Report report){
		this.report = report;
	}

	public ArrayList<Report> getReports() {
		return reports;
	}

	public void setReports(ArrayList<Report> reports) {
		this.reports = reports;
	}
	
	public void addReport(Report report){
		this.reports.add(report);
	}
	
	public Report getUUIDReport(UUID uuid){
		for (Report i : reports){
			if(i.getUuid().equals(uuid)){
				return i;
			}
		}
		return null;
	}
	
	public void removeReport(Report report){
		reports.remove(report);
	}
	
	public String toJSON(){
		Gson gson = new Gson();
		Type arrayListType = new TypeToken<ArrayList<Report>>(){}.getType();
		return gson.toJson(reports, arrayListType);
	}
	
	public void writeToFile(){
		String json = toJSON();
		
		SharedPreferences sharedPref = mAppContext.getSharedPreferences( "appData", Context.MODE_PRIVATE );
    	SharedPreferences.Editor prefEditor = sharedPref.edit();
    	prefEditor.putString("jsonArray", json);
    	prefEditor.commit();
	}
	
	private String readFromFile(){
		String result;
		SharedPreferences sharedPref = mAppContext.getSharedPreferences("appData", Context.MODE_PRIVATE);
		result = sharedPref.getString("jsonArray", "Erro");
		Log.e("teste", result);
		return result;
	}
	
	public void loadFromFile(){
		String json = readFromFile();
		if(!json.equals("Erro")){
			Gson gson = new Gson();
			Type arrayListType = new TypeToken<ArrayList<Report>>(){}.getType();
			reports = gson.fromJson(json, arrayListType);
		}
	}

}
