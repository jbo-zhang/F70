package com.hwatong.projectmode.fragment.base;

import com.hwatong.projectmode.iview.IActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends Fragment {
	protected IActivity iActivity;
	
	public void setIActivity(IActivity iActivity) {
		this.iActivity = iActivity;
	}
	
}
