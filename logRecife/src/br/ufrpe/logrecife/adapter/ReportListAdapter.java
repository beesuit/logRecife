package br.ufrpe.logrecife.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import br.ufrpe.logrecife.R;
import br.ufrpe.logrecife.model.Report;
import br.ufrpe.logrecife.util.BitmapResizeUtil;

public class ReportListAdapter extends ArrayAdapter<Report>{

	private final Context context;
	private final ArrayList<Report> array;

	public ReportListAdapter(Context context, ArrayList<Report> array) {
		super(context, R.layout.search_list_row, array);
		this.context = context;
		this.array = array;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.report_list_item, parent, false);
		Report report = array.get(position);
		
		ImageView imageView = (ImageView) v.findViewById(R.id.report_list_imageView);
		
		if(report.getPictureFile() != null){
			imageView.setImageBitmap(BitmapResizeUtil.resizeBitmap(context, report.getPictureFile()));
		}
		
		TextView textView = (TextView) v.findViewById(R.id.report_list_textView);

		textView.setText(report.getLogradouro().getLogradouro());
		
		TextView textView2 = (TextView)v.findViewById(R.id.report_list_textView2);
		if(report.isSent()){
			textView2.setText("Enviado em: " + report.getTime());
		}else{
			textView2.setText("Rascunho salvo em: " + report.getTime());
		}
		
		CheckBox checkBox = (CheckBox) v.findViewById(R.id.report_list_checkBox);
		
		checkBox.setChecked(report.isSent());

		return v;
	}

}