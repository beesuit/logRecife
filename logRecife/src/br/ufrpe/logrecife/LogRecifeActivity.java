package br.ufrpe.logrecife;

import java.util.ArrayList;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import br.ufrpe.logrecife.model.LogRecife;
import br.ufrpe.logrecife.task.GeoCoderTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class LogRecifeActivity extends FragmentActivity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private String mTitle;
	Geocoder geo;
	GoogleMap map;
	SupportMapFragment mMapFragment;
	SearchView searchView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LogRecife.get(this).loadFromFile();

		setContentView(R.layout.activity_log_recife);
		mTitle = (String) this.getTitle();

		ArrayList<String> array = new ArrayList<String>();
		array.add("Mapa");
		array.add("Reclamações");
		//array.add("Logradouro");

		
		mMapFragment = SupportMapFragment.newInstance();
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.containerTeste, mMapFragment, "map");
		fragmentTransaction.commit();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// Set the adapter for the list view
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, array));
		// Set the list's click listener
		mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView parent, View view, int position, long id){
				//TODO
				//drawerMenu

				switch(position){
				case 0:
					getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
					FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
					fragmentTransaction.replace(R.id.containerTeste, mMapFragment, "map");
					//fragmentTransaction.addToBackStack(null);
					fragmentTransaction.commit();
					mDrawerList.setItemChecked(position, true);
					mDrawerLayout.closeDrawer(mDrawerList);

					//getSupportFragmentManager().popBackStackImmediate();
					//getSupportFragmentManager().popBackStackImmediate("teste", FragmentManager.POP_BACK_STACK_INCLUSIVE);
					//mDrawerList.setItemChecked(position, true);


					break;

				case 1:
					ReportListFragment fragment = ReportListFragment.newInstance();
					getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
					FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
					ft.replace(R.id.containerTeste, fragment, "reportList");
					ft.addToBackStack(null);	
					ft.commit();
					mDrawerList.setItemChecked(position, true);
					//getActionBar().setTitle(mTitle);
					mDrawerLayout.closeDrawer(mDrawerList);
					break;

				default:
					break;
				}
			}
		});

		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				mDrawerLayout,         /* DrawerLayout object */
				R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
				R.string.drawer_open,  /* "open drawer" description */
				R.string.drawer_close  /* "close drawer" description */
				) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getActionBar().setTitle(mTitle);
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle(mTitle);
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		//getActionBar().setHomeButtonEnabled(true); api 14

	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// Set the camera to the greatest possible zoom level that includes the
		// bounds

		if (map == null) {
			map = mMapFragment.getMap();
			map.setMyLocationEnabled(true);
			LatLngBounds AUSTRALIA = new LatLngBounds(new LatLng(-8.051810,
					-34.986534), new LatLng(-8.045691, -34.878730));
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(
					AUSTRALIA.getCenter(), 11));
		}
		//TODO
		//reseta currentReport

		LogRecife.get(this).setReport(null);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.log_recife, menu);

		SearchManager searchManager = 
				(SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchView =
				(SearchView) menu.findItem(R.id.search).getActionView();
		searchView.setSearchableInfo(
				searchManager.getSearchableInfo(getComponentName()));


		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle your other action bar items...

		switch(item.getItemId()) {
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		if (Intent.ACTION_SEARCH.equals(intent.getAction())){
			String query = intent.getStringExtra(SearchManager.QUERY);
			GeoCoderTask task = GeoCoderTask.getInstance(this, query);
			task.execute();

		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}


}
