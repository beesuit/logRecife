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
import android.support.v4.app.FragmentManager;
import android.text.format.Time;
import android.util.Log;
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
	EditText editText;

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
			Log.e("teste", uuid.toString());
		}

		else if(singleton.getReport() == null){
			singleton.setReport(new Report(singleton.getLogradouro(), false));
			singleton.getReports().add(singleton.getReport());
		}
		Log.e("teste", singleton.getReport().toString());
		report = singleton.getReport();
		addressText = report.getLogradouro().getLogradouro();
		Log.e("teste", addressText);

	}



	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.e("array","Tamanho " +singleton.getReports().size());
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
		// TODO Auto-generated method stub
		inflater.inflate(R.menu.report_menu, menu);

		//MenuItem shareItem = menu.findItem(R.id.reportMenuItem1);
		//MenuItem deleteItem = menu.findItem(R.id.reportMenuItem2);

		super.onCreateOptionsMenu(menu, inflater);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()) {

		case R.id.reportMenuItem1:
			onClick();
			return true;
		case R.id.reportMenuItem2:
			onClick2();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onClick(){
		//TODO
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		shareIntent.setType("image/*");

		// For a file in shared storage.  For data in private storage, use a ContentProvider.
		Uri uri = Uri.parse("file://" + report.getPictureFile());
		shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, report.getLogradouro().getLogradouro());
		shareIntent.putExtra(Intent.EXTRA_TEXT, report.getReportText());

		startActivity(Intent.createChooser(shareIntent, "Compartilhar"));
	}

	public void onClick2(){
		//TODO
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
			//TODO
			//setPic(report.getPictureFile());
			imageView.setImageBitmap(BitmapResizeUtil.resizeBitmap(getActivity(), report.getPictureFile()));

			imageView.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
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

		//ArrayList<Item> array = LogRecife.get(this.getActivity()).getLogradouro().getItems();
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
			button.setText("Enviado");
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
			report.setEmail(data.getStringExtra(DialogFormFragment.EXTRA_EMAIL));
			report.setSent(true);

			Time today = new Time(Time.getCurrentTimezone());
			today.setToNow();
			report.setTime(today.format("%d/%m/%Y - %H:%M:%S"));

			//Log.e("teste", singleton.toJSON());
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