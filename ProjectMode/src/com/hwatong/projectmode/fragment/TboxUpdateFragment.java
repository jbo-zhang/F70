package com.hwatong.projectmode.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
	
	ProgressDialog progressDialog;
	ProgressDialog progressDialog2;

	private Object lockObject = new Object();
	
	private Object lockObject2 = new Object();
	
	private Toast mToast;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_update_tbox, container, false);

		initViews(view);

		tBoxPresenter = new TBoxPresenter(this);
		tBoxPresenter.initTboxService(getActivity());

		tBoxPresenter.loadFiles();
		return view;
	}

	private void initViews(View view) {
		lvList = (ListView) view.findViewById(R.id.lv_list);

		btLeft = (Button) view.findViewById(R.id.bt_left);
		btMiddle = (Button) view.findViewById(R.id.bt_middle);
		btRight = (Button) view.findViewById(R.id.bt_right);

		files = new ArrayList<File>();
		fileAdapter = new FileAdapter(getActivity(), files);
		lvList.setAdapter(fileAdapter);
		changeSelectedFile();

		setupClickEvent();

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
				view.setTextColor(Color.rgb(255, 255, 255));
				if (file.isDirectory()) {
					setDrawable(view, folderIcon);
				} else {
					setDrawable(view, fileIcon);
					if (selectedIndex == position)
						view.setBackgroundColor(getContext().getResources().getColor(android.R.color.holo_blue_dark));
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
		final AlertDialog.Builder normalDialog = new AlertDialog.Builder(getActivity());
		normalDialog.setTitle("TBOX升级");
		String fileSize = FileUtil.convertStorage(file.length());
		normalDialog.setMessage(file.getName() + "\n文件大小: " + fileSize + "\n确定升级TBOX吗?");
		normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				tBoxPresenter.confirmUpdate();
			}
		});
		normalDialog.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ...To-do
			}
		});
		// 显示
		normalDialog.show();
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
					if (progressDialog == null) {
						progressDialog = new ProgressDialog(getActivity());
						progressDialog.setTitle("正在复制文件...");
						progressDialog.setCanceledOnTouchOutside(true);
						progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
						progressDialog.show();
					} else {
						if (!progressDialog.isShowing()) {
							progressDialog.show();
						}
						progressDialog.setProgress((int) percent);
					}
				}
			}
		});
	}

	@Override
	public void showUpdateResult(int result, final String info) {
		synchronized (lockObject2) {
			if (progressDialog2 != null) {
				progressDialog2.setProgress(100);
				if (progressDialog2.isShowing()) {
					progressDialog2.dismiss();
				}
			}
		}
		
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
			}
		});
		
	}

	public void showToast(Context context, String msg) {
		if (mToast != null) {
			mToast.setText(msg);
			mToast.setDuration(Toast.LENGTH_SHORT);
		} else {
			mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		}
		mToast.show();
	}

	/**
	 * 复制完成
	 */
	@Override
	public void copyEnd() {
		L.d(thiz, "copyEnd()");
		synchronized (lockObject) {
			if (progressDialog != null) {
				progressDialog.setProgress(100);
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			}
		}
		L.d(thiz, "after dismiss");
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
				Toast.makeText(getActivity(), "没有找到tbox升级文件", Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * 升级开始
	 */
	@Override
	public void showUpdateStart() {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getActivity(), "开始升级", Toast.LENGTH_SHORT).show();
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
					if (progressDialog2 == null) {
						progressDialog2 = new ProgressDialog(getActivity());
						progressDialog2.setTitle("升级进度...");
						progressDialog2.setCanceledOnTouchOutside(true);
						progressDialog2.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
						progressDialog2.show();
					} else {
						if (!progressDialog2.isShowing()) {
							progressDialog2.show();
						}
						progressDialog2.setProgress((int) step);
					}
				}
			}
		});
	}

}
