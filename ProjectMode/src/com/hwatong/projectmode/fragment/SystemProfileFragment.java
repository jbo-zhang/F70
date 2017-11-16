package com.hwatong.projectmode.fragment;

import android.view.View;
import android.widget.TextView;

import com.hwatong.projectmode.R;
import com.hwatong.projectmode.fragment.base.BaseFragment;
import com.hwatong.projectmode.utils.SystemUtil;

public class SystemProfileFragment extends BaseFragment{
	
	private TextView tvCpuTemerature;
	private TextView tvRam;
	private TextView tvRom;
	private TextView tvBluetoothAddress;
	private TextView tvWlanAddress;
	private TextView tvDeviceSeries;
	private TextView tvNetSignal;
	
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_system_profile;
	}
	
	@Override
	protected void initViews(View view) {
		tvCpuTemerature = (TextView) view.findViewById(R.id.tv_cpu_temperature);
		tvRam = (TextView) view.findViewById(R.id.tv_ram);
		tvRom = (TextView) view.findViewById(R.id.tv_rom);
		tvBluetoothAddress = (TextView) view.findViewById(R.id.tv_bluetooth_address);
		tvWlanAddress = (TextView) view.findViewById(R.id.tv_wlan_address);
		tvDeviceSeries = (TextView) view.findViewById(R.id.tv_device_series);
		tvNetSignal = (TextView) view.findViewById(R.id.tv_net_signal);
		
		setViewsData();
		
	}

	private void setViewsData() {
		setCpuTemerature();
		setRam();
		setRom();
		setBluetoothAddress();
		setWlanAddress();
		setDeviceSeries();
		setNetSignal();
		
	}

	private void setCpuTemerature() {
		setFormatText(tvCpuTemerature, "32°C");
	}

	private void setRam() {
		setFormatText(tvRam, SystemUtil.getTotalRam(getActivity()));		
	}

	private void setRom() {
		setFormatText(tvRom, SystemUtil.getRomTotalSize(getActivity()));		
	}

	private void setBluetoothAddress() {
		setFormatText(tvBluetoothAddress, SystemUtil.getSystemVersion());		
	}

	private void setWlanAddress() {
		setFormatText(tvWlanAddress, SystemUtil.getSystemVersion());		
	}

	private void setDeviceSeries() {
		setFormatText(tvDeviceSeries, SystemUtil.getSystemVersion());		
	}

	private void setNetSignal() {
		setFormatText(tvNetSignal, SystemUtil.getSystemVersion());		
	}

	private void setFormatText(TextView tv, String str) {
		tv.setText(String.format((String)tv.getText(), str));
	}
	
	
	
}