package com.hwatong.projectmode.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hwatong.projectmode.R;
import com.hwatong.projectmode.fragment.base.BaseFragment;

public class UpdateFragment extends BaseFragment {

	private TextView tvSystemUpdate;
	private TextView tvTboxUpdate;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_update, container, false);
		
		initViews(view);
		
		return view;
	}

	private void initViews(View view) {
		tvSystemUpdate = (TextView) view.findViewById(R.id.tv_system_update);
		tvTboxUpdate = (TextView) view.findViewById(R.id.tv_tbox_update);
		
		setupClickEvent();
	}

	private void setupClickEvent() {
		tvSystemUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				iActivity.toSystemUpdate();
			}
		});
		
		tvTboxUpdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				iActivity.toTboxUpdate();
			}
		});
		
	}
}
