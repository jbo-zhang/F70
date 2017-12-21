package com.hwatong.projectmode.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hwatong.projectmode.R;
import com.hwatong.projectmode.fragment.base.BaseFragment;
import com.hwatong.projectmode.iview.ITboxUpdateView;
import com.hwatong.projectmode.presenter.TBoxPresenter;
import com.hwatong.projectmode.ui.ConfirmDialog;
import com.hwatong.projectmode.ui.ConfirmDialog.OnYesOnclickListener;
import com.hwatong.projectmode.ui.UpdateDialog;
import com.hwatong.projectmode.utils.FileUtil;
import com.hwatong.projectmode.utils.L;

public class TboxUpdateFragment extends BaseFragment implements ITboxUpdateView {

	private final static String thiz = TboxUpdateFragment.class.getSimpleName();

	private TBoxPresenter tBoxPresenter;
	private TextView tvTitle;
	private ListView lvList;
	private Button btLeft;
	private Button btMiddle;
	private Button btRight;

	private Drawable folderIcon, fileIcon;
	private List<File> files;
	private FileAdapter fileAdapter;

	private String currentPath = "/mnt";// Environment.getExternalStorageDirectory().getPath();

	private File currentFile;
	
	UpdateDialog copyDialog; // updateDialog;
	
	private UpdateDialog updateDialog;

	private Object lockObject = new Object();
	
	private Object lockObject2 = new Object();
	
	private int i;
	
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_update_tbox;
	}
	
	@Override
	protected void initViews(View view) {
		lvList = (ListView) view.findViewById(R.id.lv_list);

		btLeft = (Button) view.findViewById(R.id.bt_left);
		btMiddle = (Button) view.findViewById(R.id.bt_middle);
		btRight = (Button) view.findViewById(R.id.bt_right);

		files = new ArrayList<File>();
		fileAdapter = new FileAdapter(getActivity(), files);
		lvList.setAdapter(fileAdapter);
		changeSelectedFile();
		
		setupClickEvent();
		
		tBoxPresenter = new TBoxPresenter(this);
		tBoxPresenter.initTboxService(getActivity());

		tBoxPresenter.loadFiles();

	}
	
	
	@Override
	public void onDestroy() {
		L.d(thiz,"TboxUpdateFragment onDestroy !");
		tBoxPresenter.unbindTbox(getActivity());
		super.onDestroy();
	}
	

	private void setupClickEvent() {
		btLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				iActivity.toUpdate();
			}
		});

		btRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tBoxPresenter.updateTbox(currentFile);
			}
		});

		lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
				final ArrayAdapter<File> adapter = (FileAdapter) adapterView.getAdapter();
				File file = adapter.getItem(index);
				if (file.isDirectory()) {
					currentPath = file.getPath();
				} else {
					if (index != fileAdapter.getSelectedIndex()) {
						fileAdapter.setSelectedIndex(index);
						currentFile = file;
					} else {
						fileAdapter.setSelectedIndex(-1);
						currentFile = null;
					}
					adapter.notifyDataSetChanged();
					changeSelectedFile();
				}
			}
		});

	}

	/**
	 * 更改选中文件，设置确定按钮是否可用
	 * */
	private void changeSelectedFile() {
		if (btRight != null) {
			btRight.setEnabled(fileAdapter.getSelectedIndex() >= 0);
		}
	}

	/**
	 * 升级包列表适配器，给列表设置属性
	 * */
	private class FileAdapter extends ArrayAdapter<File> {

		private int selectedIndex = -1;

		public FileAdapter(Context context, List<File> files) {
			super(context, android.R.layout.simple_list_item_1, files);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView view = (TextView) super.getView(position, convertView, parent);
			File file = getItem(position);
			if (view != null) {
				view.setText(file.getName());
				view.setTextSize(20);
				view.setTextColor(Color.rgb(255, 255, 255));
				if (file.isDirectory()) {
					//setDrawable(view, folderIcon);
				} else {
					//setDrawable(view, fileIcon);
					if (selectedIndex == position)
						view.setBackgroundColor(Color.parseColor("#55625e5e"));
					else
						view.setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
				}
			}
			return view;
		}

		public void setSelectedIndex(int index) {
			selectedIndex = index;
		}

		public int getSelectedIndex() {
			return selectedIndex;
		}

		private void setDrawable(TextView view, Drawable drawable) {
			if (view != null) {
				if (drawable != null) {
					drawable.setBounds(0, 0, 60, 60);
					view.setCompoundDrawables(drawable, null, null, null);
				} else {
					view.setCompoundDrawables(null, null, null, null);
				}
			}
		}
	}
	
	
	/**
	 * 显示文件列表
	 */
	@Override
	public void showFiles(List<File> files) {
		this.files.clear();
		this.files.addAll(files);

		fileAdapter.notifyDataSetChanged();
	}
	

	/**
	 * 显示确认升级弹窗
	 */
	@Override
	public void showConfirmDialog(File file) {
		showTboxUpdateDialog(file);
	}

	private void showTboxUpdateDialog(File file) {
		
		String fileSize = FileUtil.convertStorage(file.length());
		ConfirmDialog confirmDialog = new ConfirmDialog(getActivity());
		
		confirmDialog.setYesOnclickListener(new OnYesOnclickListener() {
			
			@Override
			public void onYesClick() {
				tBoxPresenter.confirmUpdate();
			}
		});
		
		Window window = confirmDialog.getWindow();
		window.setGravity(Gravity.LEFT | Gravity.TOP);
		LayoutParams attributes = window.getAttributes();
		attributes.x = 1280/3 - 190;
		attributes.y = 80;
		window.setAttributes(attributes);
		
		confirmDialog.show();
		
		confirmDialog.setMessage(file.getName(), "文件大小: " + fileSize, "确定升级TBOX吗?");
	}
	


	/**
	 * 显示复制进度
	 */
	@Override
	public void showCopyProgress(final long percent) {
		L.d(thiz, "updateCopyProgress percent: " + percent);
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				synchronized (lockObject) {
					if (copyDialog == null) {
						copyDialog = new UpdateDialog(getActivity(), UpdateDialog.STYLE_COPY);
						Window window = copyDialog.getWindow();
						window.setGravity(Gravity.LEFT|Gravity.TOP);
						window.setLayout(571, 250);
						LayoutParams attributes = window.getAttributes();
						attributes.x = 145;
						attributes.y = 60;
						window.setAttributes(attributes);
						copyDialog.show();
					} else {
						if (!copyDialog.isShowing()) {
							copyDialog.show();
						}
						copyDialog.setProgress((int) percent);
					}
					
				}
			}
		});
	}

	/**
	 * 升级结果
	 */
	@Override
	public void showUpdateResult(int result, final String info) {
		synchronized (lockObject2) {
			if (updateDialog != null) {
				updateDialog.setProgress(100);
				if (updateDialog.isShowing()) {
					updateDialog.dismiss();
				}
			}
		}
		
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast makeText = Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT);
				makeText.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, -220, 250);
				makeText.show();
			}
		});
	}


	/**
	 * 复制完成
	 */
	@Override
	public void copyEnd() {
		L.d(thiz, "copyEnd()");
		synchronized (lockObject) {
			if (copyDialog != null) {
				copyDialog.setProgress(100);
				if (copyDialog.isShowing()) {
					L.d(thiz, "copydialog dismiss");
					copyDialog.dismiss();
				}
			}
		}
		tBoxPresenter.startUpdate();
	}

	/**
	 * 没有找到文件
	 */
	@Override
	public void showNoFiles() {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast makeText = Toast.makeText(getActivity(), "没有找到TBOX升级文件", Toast.LENGTH_SHORT);
				makeText.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, -220, 250);
				makeText.show();
			}
		});
	}

	/**
	 * 升级开始
	 */
	@Override
	public void showUpdateStart() {
		L.d(thiz, "showUpdateStart 开始升级！");
	}
	
	/**
	 * 沒有連接設備
	 */
	@Override
	public void showNoDevices() {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast makeText = Toast.makeText(getActivity(), "TBOX设备未连接", Toast.LENGTH_SHORT);
				makeText.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, -220, 250);
				makeText.show();
			}
		});
	}



	/**
	 * 显示升级进度
	 */
	@Override
	public void showUpdateProgress(final int step) {
		L.d(thiz, "showUpdateProgress step: " + step);
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				synchronized (lockObject2) {
					if (updateDialog == null) {
						updateDialog = new UpdateDialog(getActivity(), UpdateDialog.STYLE_UPDATE);
						Window window = updateDialog.getWindow();
						window.setGravity(Gravity.LEFT|Gravity.TOP);
						window.setLayout(571, 250);
						LayoutParams attributes = window.getAttributes();
						attributes.x = 145;
						attributes.y = 60;
						window.setAttributes(attributes);
						updateDialog.show();
					} else {
						if (!updateDialog.isShowing()) {
							updateDialog.show();
						}
						updateDialog.setProgress((int) step);
					}
				}
			}
		});
	}

	@Override
	public void ftpCreatFailed() {
		Toast makeText = Toast.makeText(getActivity(), "ftp目录创建失败！", Toast.LENGTH_SHORT);
		makeText.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, -220, 250);
		makeText.show();
	}

}
