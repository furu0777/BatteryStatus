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
    			
    			// �[�d�̏��
    			String statusString = null;
    			switch(status) {
    			case BatteryManager.BATTERY_HEALTH_UNKNOWN:
    				statusString= "�s��";
    				break;
    			case BatteryManager.BATTERY_STATUS_CHARGING:
    				statusString = "�[�d��";
    				break;
    			case BatteryManager.BATTERY_STATUS_DISCHARGING:
    				statusString = "���d��";
    				break;
    			case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
    				statusString = "�[�d���Ă��Ȃ�";
    				break;
    			case BatteryManager.BATTERY_STATUS_FULL:
    				statusString = "�t���[�d";
    				break;
    			}
    			
    			// �o�b�e���[�̏��
    			String healthString = null;
    			switch(health) {
    			case BatteryManager.BATTERY_HEALTH_UNKNOWN:
    				healthString = "�s��";
    				break;
    			case BatteryManager.BATTERY_HEALTH_GOOD:
    				healthString = "�ǍD";
    				break;
    			case BatteryManager.BATTERY_HEALTH_OVERHEAT:
    				healthString = "�I�[�o�[�q�[�g";
    				break;
    			case BatteryManager.BATTERY_HEALTH_DEAD:
    				healthString = "�s��";
    				break;
    			case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
    				healthString = "�d���I�[�o�[";
    				break;
    			case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
    				healthString = "�s���ȏ�Q";
    				break;
    			}
    			
    			// �o�b�e���̗L���i�L�Ftrue, ���Ffalse�j
    			//String presentString;
    			//presentString = String.valueOf(present).toString();
    			
    			// �d�r�̎c��(%)
    			String levelString;
    			levelString = String.valueOf(level).toString() + "%";
    			
    			// �d�r�̍ő�l(%)
    			//String scaleString;
    			//scaleString = String.valueOf(scale).toString();
    			
    			// �o�b�e����Ԃ̃A�C�R���̃��\�[�XID
    			//String iconSmallString;
    			//iconSmallString = String.valueOf(icon_small).toString();
    			
    			// �P�[�u���̐ڑ����
    			String acString = null;
    			switch (plugged) {
    			case BatteryManager.BATTERY_PLUGGED_AC:
    				acString = "AC�ڑ�";
    			case BatteryManager.BATTERY_PLUGGED_USB:
    				acString = "USB�ڑ�";
    				break;
    			}
    			if(acString == null){
    				acString = "�ڑ��Ȃ�";
    			}
    			
    			// �d�r�̓d��(mV)
    			String voltageString;
    			BigDecimal voltage2 = BigDecimal.valueOf(voltage);
    			voltage2 = voltage2.divide(new BigDecimal(1000), 3, BigDecimal.ROUND_HALF_UP);
    			voltageString = String.valueOf(voltage2).toString() + "V";    			
    			
    			// �d�r�̉��x(0.1��)
    			String temperatureString;
    			BigDecimal temperature2 = BigDecimal.valueOf(temperature);
    			temperature2 = temperature2.divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP);
    			temperatureString = String.valueOf(temperature2).toString() + "��";

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
