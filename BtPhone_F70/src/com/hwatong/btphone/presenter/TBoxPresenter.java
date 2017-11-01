package com.hwatong.btphone.presenter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.security.auth.Destroyable;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

import com.hwatong.btphone.iview.ITBoxUpdateView;
import com.hwatong.btphone.util.FileUtil;
import com.hwatong.btphone.util.L;
import com.tbox.service.FlowInfo;
import com.tbox.service.ITboxService;
import com.tbox.service.NetworkStatus;
import com.tbox.service.UpdateStep;

public class TBoxPresenter {

	private static final String thiz = TBoxPresenter.class.getSimpleName();

	private static final String path = "/sdcard/ftp/";
	
	private static final String ftpPath = "/sdcard/ftp";
	
	private final static String USB_PATH="/mnt/udisk";
	private final static String USB_PATH2="/mnt/udisk2";
	private final static String TFCARD_PATH="/mnt/extsd";
	
	private Context context ;
	
	private ITBoxUpdateView tboxView;
	
	// TBOX服务
	private ITboxService mTboxService;
	
	private FilenameFilter filenameFilter;

	public TBoxPresenter(ITBoxUpdateView tboxView) {
		this.tboxView = tboxView;
	}
	
	private String src, des, updateName;
	
	
	/**
	 * 初始化TBOX服务
	 */
	public void initTboxService(Context context) {
		this.context = context;
		context.bindService(new Intent("com.tbox.service.TboxService"), tboxConnection, Context.BIND_AUTO_CREATE);
		setFilter("^.*\\.tarbz2$");
		
	}
	
	public void unbindTbox(Context context) {
		if(mTboxService != null) {
			try {
				L.d(thiz, "unregisterTboxCallback");
				mTboxService.unregisterTboxCallback(mTboxCallback);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		context.unbindService(tboxConnection);
	}
	
	public void updateTbox() {
		
		List<File> files = getFiles(USB_PATH);
		if(files == null || files.size() == 0) {
			files = getFiles(USB_PATH2);
		} 
		
		if(files != null && files.size() > 0) {
			src = files.get(0).getAbsolutePath();
			des = path + files.get(0).getName();
			updateName = "/" + files.get(0).getName();
			
			File ftpDirectory = new File(ftpPath);
			if (!ftpDirectory.exists()) {
				if(!ftpDirectory.mkdirs()) {
					tboxView.showNoFiles();
					return ;
				}
			}
			tboxView.showConfirmDialog(files.get(0).getName());
		} else {
			tboxView.showNoFiles();
		}
		
		L.d(thiz, "" + files);

	}
	
	public void confirmUpdate() {
		L.d(thiz, "src: " + src + " des: " + des) ;
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				FileUtil.copyFile(src, des, tboxView);
			}
		}).start();
	}
	
	
	public void startUpdate() {
		L.d(thiz, "start update filename : " + updateName);
		if(mTboxService != null) {
			try {
				mTboxService.update(updateName);
				tboxView.showUpdateStart();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private ServiceConnection tboxConnection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			try {
				if(mTboxService != null) {
					mTboxService.unregisterTboxCallback(mTboxCallback);
					L.d(thiz, "mTboxService already unregister");
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder serv) {
			L.d(thiz, "ITboxService onServiceConnected");
			mTboxService = com.tbox.service.ITboxService.Stub.asInterface(serv);
			if (mTboxService != null) {
				try {
					int status = mTboxService.getTboxStatus();
					mTboxService.registerTboxCallback(mTboxCallback);
					L.d(thiz, "ServiceConnected getTboxStatus: " + status + "\n" + "mTboxService already register");
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			} else {
				L.d(thiz, "mTboxService is null");
			}
		}
	};

	private final com.tbox.service.ITboxCallback mTboxCallback = new com.tbox.service.ITboxCallback.Stub() {

		@Override
		public void onFlow(FlowInfo arg0) throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public void onIMEI(byte[] arg0) throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public void onIccid(byte[] arg0) throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLog(byte[] arg0) throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public void onNetworkStatusChanged(NetworkStatus arg0) throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTboxStatusChanged(int arg0) throws RemoteException {
			// TODO Auto-generated method stub

		}

		/**
		 *  0：成功
			1：失败
		 */
		@Override
		public void onUpdateCheckResult(int result) throws RemoteException {
			switch (result) {
			case 0:
				tboxView.showUpdateResult(0, "升级成功！");
				break;
			case 1:
				tboxView.showUpdateResult(0, "升級失败！");
				break;
			default:
				break;
			}
		}

		/**
		 *  0：成功
			1:文件拆包解压错误
			2:文件路径错误
			3:文件MD5校验错误
			4:文件名错误
			5:MCU升级失败
			6:SOC升级失败
			7:升级文件下载失败
		 */
		@Override
		public void onUpdateRsp(int result) throws RemoteException {
			switch (result) {
			case 0:
				tboxView.showUpdateResult(0, "成功！");
				break; 
			case 1:
				tboxView.showUpdateResult(0, "文件拆包解压错误！");
				break;
			case 2:
				tboxView.showUpdateResult(0, "文件路径错误！");
				break;
			case 3:
				tboxView.showUpdateResult(0, "文件MD5校验错误！");
				break;
			case 4:
				tboxView.showUpdateResult(0, "文件名错误！");
				break;
			case 5:
				tboxView.showUpdateResult(0, "MCU升级失败！");
				break;
			case 6:
				tboxView.showUpdateResult(0, "SOC升级失败！");
				break;
			case 7:
				tboxView.showUpdateResult(0, "升级文件下载失败！");
			break;
			default:
				break;
			}

		}

		@Override
		public void onUpdateStep(UpdateStep step) throws RemoteException {
			showToast(context, "step : " + step);
		}

		@Override
		public void onVersion(byte[] arg0) throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public void onVin(byte[] arg0) throws RemoteException {
			// TODO Auto-generated method stub

		}

	};
	
	
	 /**
     * 设置过滤字符串
     * */
    public void setFilter(final String filter) {
        filenameFilter = new FilenameFilter() {

            @Override
            public boolean accept(File file, String fileName) {
                File tempFile = new File(String.format("%s/%s", file.getPath(), fileName));
                if (tempFile.isFile())
                    return tempFile.getName().matches(filter);
                return true;
            }
        };
    }
	
	/**
     * 根据目录取升级文件，返回文件列表
     * */
    private List<File> getFiles(String directoryPath) {
    	List<File> fileList= new ArrayList<File>();
    	try {
            File directory = new File(directoryPath);
            List<File> fileList1 = Arrays.asList(directory.listFiles(filenameFilter));
            L.d(thiz,"fileList1 : " + fileList1);
            for(File f:fileList1) {
            	if (f.isFile() && FileUtil.checkUpdateFile(f)) {
            		fileList.add(f);
            	}
            }
    	}catch (Exception e) {
    		e.printStackTrace();
    	}
        return fileList;
    }
	
	
	private Toast mToast;
	public void showToast(Context context, String msg) {
		if (mToast != null) {
			mToast.setText(msg);
			mToast.setDuration(Toast.LENGTH_SHORT);
		} else {
			mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		}
		mToast.show();
	}
	
	
	

}
