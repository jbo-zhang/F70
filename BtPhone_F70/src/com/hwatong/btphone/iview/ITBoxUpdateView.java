package com.hwatong.btphone.iview;

import java.io.File;
import java.util.List;

public interface ITBoxUpdateView {
	
	void showFiles(List<File> files);
	
	void showConfirmDialog(File file);
	
	void showCopyProgress(long percent);
	
	void showUpdateResult(int result, String info);
	
	void showNoFiles();
	
	void copyEnd();
	
	void showUpdateStart();
	
	void showUpdateProgress(int step);
	
}
