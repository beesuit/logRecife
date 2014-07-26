package br.ufrpe.logrecife.model;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

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

}
