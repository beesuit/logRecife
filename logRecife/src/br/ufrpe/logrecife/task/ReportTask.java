package br.ufrpe.logrecife.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import br.ufrpe.logrecife.model.Report;

public class ReportTask extends AsyncTask<Void, Void, Void> {
	static ReportTask sTask;
	protected Context context;
	protected Report report;
	
	protected ProgressDialog atualizando;

	public ReportTask(Context c, Report report){
		context = c;
		this.report = report;
		atualizando = new ProgressDialog(context);
	}

	public static ReportTask getInstance(Context c, Report report){
		sTask = new ReportTask(c, report);
		return sTask;
	}

	@Override
	protected void onPreExecute(){
		atualizando.setMessage("Enviando...");
		atualizando.setCancelable(false);
		atualizando.show();

	}

	@Override
	protected Void doInBackground(Void... params) {
		
		//http post para o server
		

		return null;

	}

	@Override
	protected void onPostExecute(Void object) {
		atualizando.dismiss();
		Toast.makeText(context, "Enviado", Toast.LENGTH_LONG).show();
		
		//setar Report como enviado, incluir na lista de Reports
		//iniciar dialog perguntado se quer compartilhar
	}

}
