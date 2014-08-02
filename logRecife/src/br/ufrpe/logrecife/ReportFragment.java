package br.ufrpe.logrecife;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import br.ufrpe.logrecife.adapter.ListPickerAdapter;
import br.ufrpe.logrecife.model.Item;
import br.ufrpe.logrecife.model.LogRecife;
import br.ufrpe.logrecife.model.Report;
import br.ufrpe.logrecife.task.ReportTask;
import br.ufrpe.logrecife.util.BitmapResizeUtil;

public class ReportFragment extends Fragment {
	private static final int REQUEST_PHOTO = 0;
	private static final int REQUEST_FORM = 1;
	private static final String UUID = "uuid";
	private String addressText;
	private ImageView imageView;
	private ListView listView;
	private Report report;
	private LogRecife singleton;
	private EditText editText;

	public static ReportFragment newInstance(UUID uuid) {

		if (uuid == null){
			return new ReportFragment();
		}

		Bundle args = new Bundle();
		args.putSerializable(UUID, uuid);

		ReportFragment fragment = new ReportFragment();

		fragment.setArguments(args);
		fragment.setHasOptionsMenu(true);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		singleton = LogRecife.get(getActivity());

		if(getArguments() != null){
			UUID uuid = (UUID) getArguments().getSerializable(UUID);
			singleton.setReport(singleton.getUUIDReport(uuid));
		}

		else if(singleton.getReport() == null){
			singleton.setReport(new Report(singleton.getLogradouro(), false));
			singleton.getReports().add(singleton.getReport());
		}
		report = singleton.getReport();
		addressText = report.getLogradouro().getLogradouro();

	}



	@Override
	public void onPause() {
		super.onPause();
		report.setReportText(editText.getText().toString());

		if(!report.isSent()){
			Time today = new Time(Time.getCurrentTimezone());
			today.setToNow();
			report.setTime(today.format("%d/%m/%Y - %H:%M:%S"));
		}

		singleton.writeToFile();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.report_menu, menu);

		super.onCreateOptionsMenu(menu, inflater);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()) {

		case R.id.reportMenuItem1:
			share();
			return true;
		case R.id.reportMenuItem2:
			remove();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void share(){
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		shareIntent.setType("image/*");

		Uri uri = Uri.parse("file://" + report.getPictureFile());
		shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, report.getLogradouro().getLogradouro());
		shareIntent.putExtra(Intent.EXTRA_TEXT, report.getReportText());

		startActivity(Intent.createChooser(shareIntent, "Compartilhar"));
	}

	public void remove(){
		Uri uri = Uri.parse("file://" + report.getPictureFile());
		File file = new File(uri.getPath());
		boolean deleted = file.delete();

		singleton.removeReport(report);
		Toast.makeText(getActivity(), "Reclamação apagada", Toast.LENGTH_SHORT).show();
		getActivity().finish();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_report, container, false);

		editText = (EditText)v.findViewById(R.id.report_editText);
		editText.setText(report.getReportText());

		imageView = (ImageView)v.findViewById(R.id.report_imageView);

		if( report.getPictureFile() != null){
			imageView.setImageBitmap(BitmapResizeUtil.resizeBitmap(getActivity(), report.getPictureFile()));

			imageView.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					if(report.getPictureFile() != null){
						FragmentManager fm = getActivity().getSupportFragmentManager();
						ImageDialogFragment.newInstance(report.getPictureFile()).show(fm, "imageDialog");
					}

				}

			});
		}

		TextView textViewLogradouro = (TextView)v.findViewById(R.id.report_textView);
		textViewLogradouro.setText(addressText);
		textViewLogradouro.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				MapDialogFragment.newInstance(report.getLogradouro().getLatLng()).show(fm, "mapDialog");
			}

		});

		ImageButton takePictureButton = (ImageButton)v.findViewById(R.id.report_imageButton);
		takePictureButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				dispatchTakePictureIntent();
			}

		});

		listView = (ListView) v.findViewById(R.id.fragment_report_list);

		ArrayList<Item> array = report.getLogradouro().getItems();

		listView.setAdapter(new ListPickerAdapter(this.getActivity(), array));

		Button button = (Button)v.findViewById(R.id.report_sendButton);

		button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				DialogFormFragment dialog = new DialogFormFragment();
				dialog.setTargetFragment(ReportFragment.this, REQUEST_FORM);
				dialog.show(getActivity().getSupportFragmentManager(), "hey");
			}

		});

		if(report.isSent()){
			takePictureButton.setClickable(false);
			editText.setEnabled(false);
			button.setClickable(false);
			button.setText("Reclamação enviada");
		}else{
			listView.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					boolean  value = report.getLogradouro().getItems().get(position).isUserValue();
					report.getLogradouro().getItems().get(position).setUserValue(!value);
					CheckBox cb = (CheckBox) view.findViewById(R.id.list_picker_checkbox);
					cb.setChecked(!value);
				}

			});
		}

		return v;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) return;
		if (requestCode == REQUEST_PHOTO) {

			if(report.getPictureFile() != null){
				imageView.setImageBitmap(BitmapResizeUtil.resizeBitmap(getActivity(), report.getPictureFile()));
			}

		}
		else if (requestCode == REQUEST_FORM){
			report.setEmail(data.getStringExtra(DialogFormFragment.EXTRA_EMAIL));
			report.setSent(true);

			Time today = new Time(Time.getCurrentTimezone());
			today.setToNow();
			report.setTime(today.format("%d/%m/%Y - %H:%M:%S"));
			
			new ReportTask(this.getActivity(), report).execute();
			getActivity().finish();
		}
	}

	private File createImageFile() throws IOException {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment.getExternalStorageDirectory();
		File image = File.createTempFile(imageFileName, ".jpg", storageDir);

		report.setPictureFile(image.getAbsolutePath());
		return image;
	}

	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		if (takePictureIntent.resolveActivity(this.getActivity().getPackageManager()) != null) {
			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, REQUEST_PHOTO);
			}
		}
	}

}