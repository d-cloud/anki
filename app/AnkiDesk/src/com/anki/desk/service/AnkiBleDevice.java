package com.anki.desk.service;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

public class AnkiBleDevice implements Parcelable {
	private final static String TAG = "AnkiBleDevice";

	private String name;

	private String address;

	private int state;
	
	private int rssi;
	
	private BluetoothDevice device;
	
	public AnkiBleDevice(BluetoothDevice device, int state, int rssi){
		this.name = device.getName();
		this.address = device.getAddress();
		this.state = state;
		this.rssi = rssi;
		this.device = device;
	}
	
	
	public AnkiBleDevice(Parcel source) {
		this.name = source.readString();
		this.address = source.readString();
		this.state = source.readInt();
		this.rssi = source.readInt();
		this.device = source.readParcelable(AnkiBleDevice.class.getClassLoader());
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public int getState() {
		return state;
	}


	public void setState(int state) {
		this.state = state;
	}


	public int getRssi() {
		return rssi;
	}


	public void setRssi(int rssi) {
		this.rssi = rssi;
	}


	public BluetoothDevice getDevice() {
		return device;
	}


	public void setDevice(BluetoothDevice device) {
		this.device = device;
	}

	public static final Parcelable.Creator<AnkiBleDevice> CREATOR = new Parcelable.Creator() {
		public AnkiBleDevice createFromParcel(Parcel source) {
			return new AnkiBleDevice(source);
		}

		public AnkiBleDevice[] newArray(int size) {
			return new AnkiBleDevice[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(address);
		dest.writeInt(state);
		dest.writeInt(rssi);
		dest.writeParcelable(device, flags);
	}

}
