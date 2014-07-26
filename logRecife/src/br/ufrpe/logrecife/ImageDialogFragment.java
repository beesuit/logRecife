package br.ufrpe.logrecife;

import br.ufrpe.logrecife.util.BitmapResizeUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageDialogFragment extends DialogFragment {
	
	public static final String EXTRA_IMAGE_PATH = "image_path";
	
	public static ImageDialogFragment newInstance(String imagePath){
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_IMAGE_PATH, imagePath);
		
		ImageDialogFragment fragment = new ImageDialogFragment();
		fragment.setArguments(args);
		fragment.setStyle(STYLE_NO_TITLE, 0);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		ImageView imageView = new ImageView(this.getActivity());
		String path = this.getArguments().getString(EXTRA_IMAGE_PATH);
		imageView.setImageBitmap(BitmapResizeUtil.resizeBitmapForDialog(getActivity(), path));
		
		return imageView;
	}
	
	

}
