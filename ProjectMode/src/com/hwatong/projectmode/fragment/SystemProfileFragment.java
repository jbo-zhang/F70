package com.hwatong.projectmode.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hwatong.projectmode.R;
import com.hwatong.projectmode.fragment.base.BaseFragment;

public class SystemProfileFragment extends BaseFragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_system_profile, container, false);
	}
	
}
