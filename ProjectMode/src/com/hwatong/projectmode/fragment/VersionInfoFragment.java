package com.hwatong.projectmode.fragment;

import android.view.View;
import android.widget.TextView;

import com.hwatong.projectmode.R;
import com.hwatong.projectmode.fragment.base.BaseFragment;
import com.hwatong.projectmode.utils.SystemUtil;


public class VersionInfoFragment extends BaseFragment{
	private TextView tvProductModel;
	private TextView tvAndroidVersion;
	private TextView tvMcuVersion;
	private TextView tvArmVersion;
	private TextView tvMapVersion;
	private TextView tvBluetoothVersion;
	private TextView tvSoundVersion;
	
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_version_info;
	}
	
	@Override
	protected void initViews(View view) {
		tvProductModel = (TextView) view.findViewById(R.id.tv_product_model);
		tvAndroidVersion = (TextView) view.findViewById(R.id.tv_android_version);
		tvMcuVersion = (TextView) view.findViewById(R.id.tv_mcu_version);
		tvArmVersion = (TextView) view.findViewById(R.id.tv_arm_version);
		tvMapVersion = (TextView) view.findViewById(R.id.tv_map_version);
		tvBluetoothVersion = (TextView) view.findViewById(R.id.tv_bluetooth_version);
		tvSoundVersion = (TextView) view.findViewById(R.id.tv_sound_version);
		
		setViewsData();
		
	}

	private void setViewsData() {
		setProductModel();
		setAndroidVersion();
		setMcuVersion();
		setArmVersion();
		setMapVersion();
		setBluetoothVersion();
		setSoundVersion();
		
	}

	private void setProductModel() {
		setFormatText(tvProductModel, "F70");
	}

	private void setAndroidVersion() {
		setFormatText(tvAndroidVersion, SystemUtil.getSystemVersion());
	}

	private void setMcuVersion() {
		setFormatText(tvMcuVersion, SystemUtil.getMcuVersionInfo());
	}

	private void setArmVersion() {
		setFormatText(tvArmVersion, SystemUtil.getSoftwareVersion());
	}

	private void setMapVersion() {
		setFormatText(tvMapVersion, "地图版本号");
	}

	private void setBluetoothVersion() {
		setFormatText(tvBluetoothVersion, "蓝牙固件版本号");
	}

	private void setSoundVersion() {
		setFormatText(tvSoundVersion, "语音版本号");
	}
	
	private void setFormatText(TextView tv, String str) {
		tv.setText(String.format((String)tv.getText(), str));
	}
	

}
