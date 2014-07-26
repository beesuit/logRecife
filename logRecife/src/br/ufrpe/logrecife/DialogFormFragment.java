package br.ufrpe.logrecife;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

public class DialogFormFragment extends DialogFragment {
	
	public static final String EXTRA_CPF = "cpf";
	public static final String EXTRA_EMAIL = "email";

	//test
	private String cpf;
	private String email;

	public DialogFormFragment() {
		// TODO Auto-generated constructor stub
		cpf = "cpf";
		email = "email";
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_send_form, null);
		
		return new AlertDialog.Builder(getActivity())
        .setView(v)
        .setTitle("Enviar")
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
        	@Override
            public void onClick(DialogInterface dialog, int which) {
                sendResult(Activity.RESULT_OK);
            }
        })
        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				sendResult(Activity.RESULT_CANCELED);
			}
		})
        .create();
	}
	
	private void sendResult(int resultCode) {
        if (getTargetFragment() == null) 
            return;

        Intent i = new Intent();
        i.putExtra(EXTRA_CPF, cpf);
        i.putExtra(EXTRA_EMAIL, email);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }
	
	

}
