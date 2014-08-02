package br.ufrpe.logrecife;

import java.util.ArrayList;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
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
import br.ufrpe.logrecife.task.ReverseGeoCoderTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class LogRecifeActivity extends FragmentActivity {
	private final String MAP_FRAGMENT_TAG = "map";
	private final String REPORT_LIST_FRAGMENT_TAG = "reportList";
	
	private final double RECIFE_SOUTHWEST_LAT = -8.051810;
	private final double RECIFE_SOUTHWEST_LNG = -34.986534;
	private final double RECIFE_NORTHEAST_LAT = -8.045691;
	private final double RECIFE_NORTHEAST_LNG = -34.878730;
	private final float RECIFE_ZOOM_LEVEL = 11;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private String mTitle;
	private GoogleMap map;
	private SupportMapFragment mMapFragment;
	private SearchView searchView;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LogRecife.get(this).loadFromFile();

		setContentView(R.layout.activity_logrecife);
		mTitle = (String) this.getTitle();

		mMapFragment = SupportMapFragment.newInstance();
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.activity_logrecife_container, mMapFragment, MAP_FRAGMENT_TAG);
		fragmentTransaction.commit();

		setUpDrawer();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		setUpMap();
		LogRecife.get(this).setReport(null);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.log_recife, menu);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchView =(SearchView) menu.findItem(R.id.search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		
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
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private void doFragmentTransaction(Fragment fragment, String tag, boolean backstack){

		if(backstack){
			getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.activity_logrecife_container, fragment, tag);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		}else{
			getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.activity_logrecife_container, fragment, tag);
			fragmentTransaction.commit();
		}

	}
	
	private void setUpMap(){
		if (map == null) {
			map = mMapFragment.getMap();

			map.setOnMapClickListener(new OnMapClickListener(){

				@Override
				public void onMapClick(LatLng latLng) {
					new ReverseGeoCoderTask(LogRecifeActivity.this, latLng).execute();
				}

			});
			
			map.setMyLocationEnabled(true);
			LatLngBounds RECIFE = new LatLngBounds(new LatLng(RECIFE_SOUTHWEST_LAT,
					RECIFE_SOUTHWEST_LNG), new LatLng(RECIFE_NORTHEAST_LAT, RECIFE_NORTHEAST_LNG));
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(
					RECIFE.getCenter(), RECIFE_ZOOM_LEVEL));
		}
	}
	
	private void setUpDrawer(){
		
		ArrayList<String> menuItems = new ArrayList<String>();
		menuItems.add("Mapa");
		menuItems.add("Reclamações");
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_logrecife_drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.activity_logrecife_left_drawer);
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, menuItems));
		mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView parent, View view, int position, long id){
				switch(position){
				case 0:

					doFragmentTransaction(mMapFragment, MAP_FRAGMENT_TAG, false);
					mDrawerList.setItemChecked(position, true);
					mDrawerLayout.closeDrawer(mDrawerList);
					break;

				case 1:

					ReportListFragment fragment = ReportListFragment.newInstance();
					doFragmentTransaction(fragment, REPORT_LIST_FRAGMENT_TAG, true);
					mDrawerList.setItemChecked(position, true);
					mDrawerLayout.closeDrawer(mDrawerList);
					break;

				default:
					break;
				}
			}
		});

		mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.drawable.ic_drawer,R.string.drawer_open,R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getActionBar().setTitle(mTitle);
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle(mTitle);
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}


}
