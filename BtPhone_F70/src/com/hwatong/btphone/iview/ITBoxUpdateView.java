package com.hwatong.btphone.iview;

public interface ITBoxUpdateView {
	void showConfirmDialog(String fileName);
	void showCopyProgress(long percent);
	void showUpdateResult(int result, String info);
	
	void showNoFiles();
	
	void copyEnd();
	
	void showUpdateStart();
}
