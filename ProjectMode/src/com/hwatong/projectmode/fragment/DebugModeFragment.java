package com.hwatong.projectmode.fragment;

import android.os.SystemProperties;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.hwatong.projectmode.R;
import com.hwatong.projectmode.fragment.base.BaseFragment;
import com.hwatong.projectmode.ui.SwitchButton;
import com.hwatong.projectmode.ui.SwitchButton.OnUserCheckedChangeListener;
import com.hwatong.projectmode.utils.L;

/**
 * 	属性persist.sys.log.config和sys.log.config控制系统日志打印，persist.sys.log.config属性重启后仍生效sys.log.config只在本次开机时间内有效
 *  属性persist.sys.gps.log和sys.gps.log控制gps日志打印，persist.sys.gps.log属性重启后仍生效sys.gps.log只在本次开机时间内有效
 * @author zxy time:2017年11月21日
 *
 */

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
        
        sbSystemLogs.setChecked(SystemProperties.getBoolean("persist.sys.log.config", false));
        
        sbGpsLogs.setChecked(SystemProperties.getBoolean("persist.sys.gps.log", false));
        
        sbSystemLogs.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				L.d(thiz, "persist.sys.log.config : " + isChecked);
				SystemProperties.set("persist.sys.log.config", isChecked + "");
			}
		});
        
        sbGpsLogs.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				L.d(thiz, "persist.sys.gps.log : " + isChecked);
				SystemProperties.set("persist.sys.gps.log", isChecked + "");			
			}
		});
        
        
	}
	
	
	
	
}
