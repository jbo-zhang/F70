package com.hwatong.projectmode.fragment;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.hwatong.projectmode.R;
import com.hwatong.projectmode.fragment.base.BaseFragment;
import com.hwatong.projectmode.ui.SwitchButton;
import com.hwatong.projectmode.ui.SwitchButton.OnUserCheckedChangeListener;
import com.hwatong.projectmode.utils.L;

public class DebugModeFragment extends BaseFragment{
	
	private static final String thiz = DebugModeFragment.class.getSimpleName();
	
	private SwitchButton sbAdbDebug;
	private SwitchButton sbSystemLogs;
	private SwitchButton sbTboxLogs;
	private SwitchButton sbGpsLogs;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_debug_mode;
	}
	
	@Override
	protected void initViews(View view) {
        sbAdbDebug = (SwitchButton) view.findViewById(R.id.switch_adb_debug);
        sbSystemLogs = (SwitchButton) view.findViewById(R.id.switch_system_logs);
        sbTboxLogs = (SwitchButton) view.findViewById(R.id.switch_tbox_logs);
        sbGpsLogs = (SwitchButton) view.findViewById(R.id.switch_gps_logs);
        
        sbAdbDebug.setChecked(false);
        
        
        sbAdbDebug.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				L.d(thiz, "onCheckedChanged isChecked : " + isChecked);				
			}
		});
        
        
	}
	
	
	
	
}
