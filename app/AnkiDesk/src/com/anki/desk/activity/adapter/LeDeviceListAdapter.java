package com.anki.desk.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.anki.desk.R;
import com.anki.desk.service.AnkiBleDevice;
import com.anki.desk.service.BleService;

public class LeDeviceListAdapter extends BaseAdapter {
	
	private ArrayList<AnkiBleDevice> mLeDevices = new ArrayList<AnkiBleDevice>();
    private LayoutInflater mInflator;
    private Context context;

    public LeDeviceListAdapter(Context context,LayoutInflater mInflator) {
        super();
        this.mInflator = mInflator;
        this.context = context;
        mLeDevices = new ArrayList<AnkiBleDevice>();
    }

    public void addDevices(AnkiBleDevice d) {
        mLeDevices.add(d);
    }
    
    public void addAllDevices(List<AnkiBleDevice> list) {
        mLeDevices.addAll(list);
    }
    
    
    public void clear() {
        mLeDevices.clear();
    }

    @Override
    public int getCount() {
        return mLeDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return mLeDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = mInflator.inflate(R.layout.ble_listview, null);
            viewHolder = new ViewHolder();
            viewHolder.deviceAddress = (TextView) view.findViewById(R.id.ble_address);
            viewHolder.deviceName = (TextView) view.findViewById(R.id.ble_name);
            viewHolder.conBtn = (Button) view.findViewById(R.id.connectBtn);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final AnkiBleDevice device = mLeDevices.get(position);
        final String deviceName = device.getName();
        if (deviceName != null && deviceName.length() > 0)
            viewHolder.deviceName.setText(deviceName);
        else
            viewHolder.deviceName.setText("Unkown");
        viewHolder.deviceAddress.setText(device.getAddress());
        
        viewHolder.conBtn.setText(getBtnText(device));
        viewHolder.conBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent connectIntent = new Intent(context, BleService.class);
				connectIntent.putExtra(BleService.bleAddress, device.getAddress());
				if(device.getState()==BluetoothProfile.STATE_DISCONNECTED){
					connectIntent.setAction(BleService.CONNECT_BlE);
				}else if(device.getState()==BluetoothProfile.STATE_CONNECTED){
					connectIntent.setAction(BleService.DISCONNECT_BLE);
				}
				context.startService(connectIntent);
			}
		});

        return view;
	}

	private String getBtnText(AnkiBleDevice device) {
		String text = context.getString(R.string.connect);
		if(device.getState()==BluetoothProfile.STATE_DISCONNECTING){
			text = context.getString(R.string.connecting);
		}else if(device.getState()==BluetoothProfile.STATE_CONNECTED){
			text = context.getString(R.string.dis_connect);
		}else if(device.getState()==BluetoothProfile.STATE_DISCONNECTING){
			text = context.getString(R.string.dis_connecting);
		}
		return text;
	}
}


class ViewHolder {
    TextView deviceName;
    TextView deviceAddress;
    Button conBtn;
}
