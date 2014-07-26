package br.ufrpe.logrecife;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Launcher extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.launcher);
	}
	
	public void launchApp1(View v){
		Intent i = new Intent(this, LogRecifeActivity.class);
		this.startActivity(i);
	}

}
