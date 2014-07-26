package br.ufrpe.logrecife.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import br.ufrpe.logrecife.R;

public class BitmapResizeUtil {

	public BitmapResizeUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static Bitmap resizeBitmap(Context context, String filepath) {
		// Get the dimensions of the View
		int targetW = (int) context.getResources().getDimension(R.dimen.report_fragment_imageView_width); 
		int targetH = (int) context.getResources().getDimension(R.dimen.report_fragment_imageView_height);

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

		return orientedBitmap;
	}
	
	public static Bitmap resizeBitmapForDialog(Context context, String filepath) {
		// Get the dimensions of the Display
		int targetW = context.getResources().getDisplayMetrics().widthPixels; 
		int targetH = context.getResources().getDisplayMetrics().heightPixels;

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

		return orientedBitmap;
	}

}
