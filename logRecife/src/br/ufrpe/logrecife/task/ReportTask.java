package br.ufrpe.logrecife.task;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;
import br.ufrpe.logrecife.model.Report;
//TODO done
public class ReportTask extends AsyncTask<Void, Void, Void> {
	static ReportTask sTask;
	protected Context context;
	protected Report report;
	
	protected ProgressDialog sending;

	public ReportTask(Context c, Report report){
		context = c;
		this.report = report;
		sending = new ProgressDialog(context);
	}

	public static ReportTask getInstance(Context c, Report report){
		sTask = new ReportTask(c, report);
		return sTask;
	}

	@Override
	protected void onPreExecute(){
		sending.setMessage("Enviando...");
		sending.setCancelable(false);
		sending.show();

	}

	@Override
	protected Void doInBackground(Void... params) {
		
		if (false & report.getPictureFile() != null) {
			Uri uri = Uri.parse("file://" + report.getPictureFile());
			File file = new File(uri.getPath());
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://192.168.137.1:8080/siglog/android");
			HttpEntity httpEntity = MultipartEntityBuilder
					.create()
					.addBinaryBody("file", file,
							ContentType.create("image/jpeg"),
							report.getUuid().toString())
					.addTextBody("reportText", report.getReportText()).build();
			httppost.setEntity(httpEntity);
			HttpResponse response;
			try {
				response = httpclient.execute(httppost);
				HttpEntity resEntity = response.getEntity();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;

	}

	@Override
	protected void onPostExecute(Void object) {
		sending.dismiss();
		Toast.makeText(context, "Reclamação enviada", Toast.LENGTH_LONG).show();
	}

}
