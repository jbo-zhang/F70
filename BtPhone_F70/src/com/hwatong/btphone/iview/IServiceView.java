package com.hwatong.btphone.iview;

import com.hwatong.btphone.bean.CallLog;

public interface IServiceView {
	void showWindow(CallLog callLog);
	void hideWindow();
	void showTalking(CallLog callLog);
}
