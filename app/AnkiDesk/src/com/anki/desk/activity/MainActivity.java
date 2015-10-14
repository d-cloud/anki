package com.anki.desk.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.anki.desk.R;
import com.anki.desk.activity.adapter.LeDeviceListAdapter;
import com.anki.desk.service.AnkiBleDevice;
import com.anki.desk.service.BleService;

public class MainActivity extends Activity {

	private final static String TAG = "MainActivity: ";
	
	private ArrayList<AnkiBleDevice> abdList = new ArrayList<AnkiBleDevice>();
	
	private ListView bleListView = null; 
	
	private Button refreshBtn = null;
	
	private LeDeviceListAdapter listViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PlaceholderFragment mainFragment = new PlaceholderFragment();
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, mainFragment).commit();
		}
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, "BLE not Surpot", Toast.LENGTH_SHORT).show();
			finish();
		}else{
			
			
			//绑定服务
			bindService(new Intent(BleService.ACTION), conn, BIND_AUTO_CREATE);
			//开始服务
			startService(new Intent(BleService.ACTION));
			//stopService(new Intent(BleService.ACTION));
			Intent connectIntent = new Intent(MainActivity.this, BleService.class);
			connectIntent.setAction(BleService.CONNECT_BlE);
			connectIntent.putExtra(BleService.bleAddress, "aaaaaaaaaaaaaa");
			startService(connectIntent);
			
			IntentFilter filter = new IntentFilter();
			filter.addAction(BleService.BROADCAST_ACTION);
			registerReceiver(bleMapReceiver, filter);
		}
	}
	
	

	@Override
	protected void onDestroy() {
		unregisterReceiver(bleMapReceiver);
		super.onDestroy();
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	

	ServiceConnection conn = new ServiceConnection() {
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.v(TAG, "onServiceConnected");
		}

		public void onServiceDisconnected(ComponentName name) {
			Log.v(TAG, "onServiceDisconnected");
		}
	};
	BroadcastReceiver bleMapReceiver = new BroadcastReceiver() {
		//蓝牙列表发生变化
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(BleService.BROADCAST_ACTION)){
				boolean isList = intent.getBooleanExtra("isList", false);
				if(isList){
					abdList = intent.getParcelableArrayListExtra("abdList");
					listViewAdapter.clear();
					listViewAdapter.addAllDevices(abdList);
				}else{
					boolean isBegin = intent.getBooleanExtra("isBegin", false);
					boolean isEnd = intent.getBooleanExtra("isEnd", false);
					AnkiBleDevice device = intent.getParcelableExtra("device");
					System.out.println(TAG + "update ui,isBegin:"+isBegin+",isEnd:"+isEnd+",device:"+device);
					if(isBegin){
						abdList.clear();
						listViewAdapter.clear();
					}else if(isEnd){
						return;
					}else if(device!=null){
						listViewAdapter.addDevices(device);
						abdList.add(device);
					}
				}
				listViewAdapter.notifyDataSetChanged();
			}
		} 
	};
	

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public  class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			bleListView = (ListView) getView().findViewById(R.id.ble_list_view);
			listViewAdapter = new LeDeviceListAdapter(MainActivity.this,getLayoutInflater());
			bleListView.setAdapter(listViewAdapter);
			
			refreshBtn = (Button) getView().findViewById(R.id.refresh_btn);
			refreshBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent connectIntent = new Intent(MainActivity.this, BleService.class);
					connectIntent.setAction(BleService.REFRESH);
					startService(connectIntent);
				}
			});
			
			super.onActivityCreated(savedInstanceState);
		}
		
		
	}
}

