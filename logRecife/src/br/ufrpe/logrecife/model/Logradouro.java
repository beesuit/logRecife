package br.ufrpe.logrecife.model;

import java.util.ArrayList;
import android.location.Address;
import com.google.android.gms.maps.model.LatLng;

public class Logradouro {
	private Address address;
	private LatLng latLng;
	private ArrayList<Item> items;

	public Logradouro(Address address, LatLng latLng, ArrayList<Item> items) {
		this.address = address;
		this.latLng = latLng;
		this.items = items;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public LatLng getLatLng() {
		return latLng;
	}

	public void setLatLng(LatLng latLng) {
		this.latLng = latLng;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}
	
	public String getLogradouro(){
		return address.getAddressLine(0);
	}
	

}
