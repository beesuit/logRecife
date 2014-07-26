package br.ufrpe.logrecife;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import br.ufrpe.logrecife.adapter.ListPickerAdapter;
import br.ufrpe.logrecife.model.Item;
import br.ufrpe.logrecife.model.LogRecife;
import br.ufrpe.logrecife.model.Report;
import br.ufrpe.logrecife.util.BitmapResizeUtil;
import br.ufrpe.logrecife.util.ExifUtil;

public class ReportFragment extends Fragment {
	private static final int REQUEST_PHOTO = 0;
	private static final int REQUEST_FORM = 1;
	private static final String UUID = "uuid";
	private String addressText;
	ImageView imageView;
	ListView listView;
	Report report;
	LogRecife singleton;

	public static ReportFragment newInstance(UUID uuid) {
		
		if (uuid == null){
			return new ReportFragment();
		}
		
		Bundle args = new Bundle();
		args.putSerializable(UUID, uuid);

		ReportFragment fragment = new ReportFragment();

		fragment.setArguments(args);

		return fragment;
	}
	//TODO
	//public static ReportFragment newInstance() {
	//return new ReportFragment();
	//}

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
		// TODO Auto-generated method stub
		super.onPause();
		Log.e("array","Tamanho " +singleton.getReports().size());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_report, container, false);

		imageView = (ImageView)v.findViewById(R.id.report_imageView);

		if( report.getPictureFile() != null){
			//TODO
			//setPic(report.getPictureFile());
			imageView.setImageBitmap(BitmapResizeUtil.resizeBitmap(getActivity(), report.getPictureFile()));
		}

		TextView textViewLogradouro = (TextView)v.findViewById(R.id.report_textView);
		textViewLogradouro.setText(addressText);

		ImageButton takePictureButton = (ImageButton)v.findViewById(R.id.report_imageButton);
		takePictureButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				dispatchTakePictureIntent();
			}

		});

		listView = (ListView) v.findViewById(R.id.fragment_report_list);

		ArrayList<Item> array = LogRecife.get(this.getActivity()).getLogradouro().getItems();

		listView.setAdapter(new ListPickerAdapter(this.getActivity(), array));

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
			button.setClickable(false);
			button.setText("Enviado");
		}

		return v;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) return;
		if (requestCode == REQUEST_PHOTO) {

			if(report.getPictureFile() != null){
				//TODO
				//setPic(report.getPictureFile());
				imageView.setImageBitmap(BitmapResizeUtil.resizeBitmap(getActivity(), report.getPictureFile()));
			}

			//Bundle extras = data.getExtras();
			//thumbnail = (Bitmap) extras.get("data");
			//imageView.setImageBitmap(thumbnail);
		}
		else if (requestCode == REQUEST_FORM){
			//TODO
			//salvar email e cpf no modelo, usar async task para postar report no server e no caso setar como
			//enviada
			report.setCpf(data.getStringExtra(DialogFormFragment.EXTRA_CPF));
			report.setEmail(data.getStringExtra(DialogFormFragment.EXTRA_EMAIL));
			singleton.addReport(report);
		}
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment.getExternalStorageDirectory();
		File image = File.createTempFile(
				imageFileName,  /* prefix */
				".jpg",         /* suffix */
				storageDir      /* directory */
				);

		// Save a file: path for use with ACTION_VIEW intents
		report.setPictureFile(image.getAbsolutePath()); 
		return image;
	}

	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(this.getActivity().getPackageManager()) != null) {
			// Create the File where the photo should go
			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException ex) {
				// Error occurred while creating the File

			}
			// Continue only if the File was successfully created
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, REQUEST_PHOTO);
			}
		}
	}
	//TODO
	// remover método
	private void setPic(String filepath) {

		// Get the dimensions of the View
		int targetW = (int) getResources().getDimension(R.dimen.report_fragment_imageView_width); 
		int targetH = (int) getResources().getDimension(R.dimen.report_fragment_imageView_height);

		// Get the dimensions of the bitmap
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(filepath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		// Determine how much to scale down the image
		int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

		// Decode the image file into a Bitmap sized to fill the View
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		bitmap = BitmapFactory.decodeFile(filepath, bmOptions);
		//rotate using ExifUtil
		Bitmap orientedBitmap = ExifUtil.rotateBitmap(filepath, bitmap);


		imageView.setImageBitmap(orientedBitmap);
	}

}