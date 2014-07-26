package br.ufrpe.logrecife;

import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;


public class ReportActivity extends FragmentActivity {
	private UUID uuid;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_fragment);
		
		if(getIntent() != null){
			uuid = (UUID) getIntent().getSerializableExtra(ReportListFragment.UUID);
			//Log.e("teste", uuid.toString());
		}
		
		FragmentManager manager = getSupportFragmentManager();
		Fragment fragment = manager.findFragmentById(R.id.single_container);

		if (fragment == null) {
			fragment = ReportFragment.newInstance(uuid);
			manager.beginTransaction()
			.add(R.id.single_container, fragment)
			.commit();
		}


	}

}
