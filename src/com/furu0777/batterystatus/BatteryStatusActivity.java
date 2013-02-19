package com.furu0777.batterystatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.BatteryManager;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.SimpleAdapter;

public class BatteryStatusActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_status);
    }

    @Override
    protected void onResume() {
    	super.onResume();
    	
    	IntentFilter filter = new IntentFilter();
    	
    	filter.addAction(Intent.ACTION_BATTERY_CHANGED);
    	registerReceiver(mBroadcastReceiver, filter);
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	
    	unregisterReceiver(mBroadcastReceiver);
    }
    
    
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
    	public void onReceive(Context context, Intent intent) {
    		String action = intent.getAction();
    		if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
    			int status = intent.getIntExtra("status", 0);
    			int health = intent.getIntExtra("health", 0);
    			//boolean present = intent.getBooleanExtra("present", false);
    			int level = intent.getIntExtra("level", 0);
    			//int scale = intent.getIntExtra("scale", 0);
    			//int icon_small = intent.getIntExtra("icon-small", 0);
    			int plugged = intent.getIntExtra("plugged", 0);
    			int voltage = intent.getIntExtra("voltage", 0);
    			int temperature = intent.getIntExtra("temperature", 0);
    			String technology = intent.getStringExtra("technology");
    			
    			// 充電の状態
    			String statusString = null;
    			switch(status) {
    			case BatteryManager.BATTERY_HEALTH_UNKNOWN:
    				statusString= "不明";
    				break;
    			case BatteryManager.BATTERY_STATUS_CHARGING:
    				statusString = "充電中";
    				break;
    			case BatteryManager.BATTERY_STATUS_DISCHARGING:
    				statusString = "放電中";
    				break;
    			case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
    				statusString = "充電していない";
    				break;
    			case BatteryManager.BATTERY_STATUS_FULL:
    				statusString = "フル充電";
    				break;
    			}
    			
    			// バッテリーの状態
    			String healthString = null;
    			switch(health) {
    			case BatteryManager.BATTERY_HEALTH_UNKNOWN:
    				healthString = "不明";
    				break;
    			case BatteryManager.BATTERY_HEALTH_GOOD:
    				healthString = "良好";
    				break;
    			case BatteryManager.BATTERY_HEALTH_OVERHEAT:
    				healthString = "オーバーヒート";
    				break;
    			case BatteryManager.BATTERY_HEALTH_DEAD:
    				healthString = "不良";
    				break;
    			case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
    				healthString = "電圧オーバー";
    				break;
    			case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
    				healthString = "不明な障害";
    				break;
    			}
    			
    			// バッテリの有無（有：true, 無：false）
    			//String presentString;
    			//presentString = String.valueOf(present).toString();
    			
    			// 電池の残量(%)
    			String levelString;
    			levelString = String.valueOf(level).toString() + "%";
    			
    			// 電池の最大値(%)
    			//String scaleString;
    			//scaleString = String.valueOf(scale).toString();
    			
    			// バッテリ状態のアイコンのリソースID
    			//String iconSmallString;
    			//iconSmallString = String.valueOf(icon_small).toString();
    			
    			// ケーブルの接続状態
    			String acString = null;
    			switch (plugged) {
    			case BatteryManager.BATTERY_PLUGGED_AC:
    				acString = "AC接続";
    			case BatteryManager.BATTERY_PLUGGED_USB:
    				acString = "USB接続";
    				break;
    			}
    			if(acString == null){
    				acString = "接続なし";
    			}
    			
    			// 電池の電圧(mV)
    			String voltageString;
    			BigDecimal voltage2 = BigDecimal.valueOf(voltage);
    			voltage2 = voltage2.divide(new BigDecimal(1000), 3, BigDecimal.ROUND_HALF_UP);
    			voltageString = String.valueOf(voltage2).toString() + "V";    			
    			
    			// 電池の温度(0.1℃)
    			String temperatureString;
    			BigDecimal temperature2 = BigDecimal.valueOf(temperature);
    			temperature2 = temperature2.divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP);
    			temperatureString = String.valueOf(temperature2).toString() + "℃";

    			String[] menuItems = getResources().getStringArray(R.array.menu_items);
       			String[] menuSubItems = {statusString,
    					healthString,
    					levelString,
    					acString,
    					voltageString,
    					temperatureString,
    					technology
    					};
    			
    			List<Map<String, String>> dataList = new ArrayList<Map<String,String>>();    			
    	        for(int i = 0; i < menuItems.length; i++) {
    	            Map<String, String> data = new HashMap<String, String>();
    	            data.put("title", menuItems[i]);
    	            data.put("comment", menuSubItems[i]);
    	            dataList.add(data);
    	        }
    			
    	        SimpleAdapter adapter = new SimpleAdapter(
    	                context,
    	                dataList,
    	                android.R.layout.simple_list_item_2,
    	                new String[] { "title", "comment" },
    	                new int[] { android.R.id.text1, android.R.id.text2 }
    	            );
    	        setListAdapter(adapter);
    		}
    	}
    };
}
