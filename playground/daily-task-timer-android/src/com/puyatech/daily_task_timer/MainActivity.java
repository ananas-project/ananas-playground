package com.puyatech.daily_task_timer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import ananas.lib.util.logging.AbstractLoggerFactory;
import ananas.lib.util.logging.Logger;
import ananas.tools.dtt.Const;
import ananas.tools.dtt.DailyContext;
import ananas.tools.dtt.DailyController;
import ananas.tools.dtt.DailyTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

public class MainActivity extends Activity {

	final static Logger logger = (new AbstractLoggerFactory() {
	}).getLogger();

	private ListView mListView;
	private MyListAdapter mListAdapter;
	private MyTimer mTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ListView lv = (ListView) this.findViewById(R.id.listView1);
		this.mListView = lv;

		this.mTimer = new MyTimer(new Runnable() {

			@Override
			public void run() {
				MainActivity.this.onTimer();
			}
		});

		MyListAdapter adp = this.newAdapter();
		lv.setAdapter(adp);
		this.mListAdapter = adp;

	}

	protected void onTimer() {
		// System.out.println(this + ".onTimer");
		this.mListAdapter.notifyDataSetChanged();
	}

	private MyListAdapter newAdapter() {

		Properties prop = new Properties();

		File dir = this.getWorkingDir();
		String strConf = (new File(dir, "config.xml")).getAbsolutePath();
		String strCurr = (new File(dir, "rec.txt")).getAbsolutePath();
		prop.setProperty(Const.key_file_conf, strConf);
		prop.setProperty(Const.key_file_current, strCurr);
		DailyContext dcf = DailyContext.Factory.getDefault(prop);

		try {
			File file = dcf.getConfigFile();
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
				this.writeDefaultConfig(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		DailyController ctrl = dcf.getFactory().newController();

		ctrl.load();
		return new MyListAdapter(this, ctrl);
	}

	private File getWorkingDir() {
		File dir = android.os.Environment.getExternalStorageDirectory();
		return new File(dir, "ananas/daily-task-timer");
	}

	@Override
	protected void onPause() {
		this.mTimer.stop();
		super.onPause();
	}

	@Override
	protected void onResume() {
		this.mTimer.start();
		super.onResume();
	}

	class MyListAdapter extends ArrayAdapter<DailyTask> implements
			OnClickListener {

		static final int id_layout = android.R.layout.simple_list_item_checked;
		private final DailyController mDailyCtrl;

		public MyListAdapter(Context context, DailyController ctrl) {
			super(context, id_layout, ctrl.getModel().getTasks());
			this.mDailyCtrl = ctrl;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup parent) {

			CheckedTextView view = (CheckedTextView) super.getView(pos,
					convertView, parent);

			// System.out.println(view + "");
			DailyTask item = this.getItem(pos);

			long nTimeSpan = item.getTimeSpan(false);
			long nTimeQuota = item.getTimeQuota();
			String strTimeSpan = this.formatTimeInterval(nTimeSpan);
			String strTimeQuota = this.formatTimeInterval(nTimeQuota);

			String timeout = "";
			if (nTimeQuota < nTimeSpan) {
				timeout = "timeout";
			}

			view.setText(item.getName() + " [" + strTimeSpan + "/"
					+ strTimeQuota + "] " + timeout);

			DailyTask hotTask = this.mDailyCtrl.getModel().getCurrentTask();
			view.setChecked(item.equals(hotTask));

			view.setOnClickListener(this);
			view.setId(pos);

			return view;
		}

		private String formatTimeInterval(long ti /* timeInterval */) {

			int tt = (int) (ti / 1000);

			int ss = tt % 60;
			int mm = (tt / 60) % 60;
			int hh = tt / 3600;

			return (hh + ":" + mm + ":" + ss);
		}

		public DailyController getDailyCtrl() {
			return this.mDailyCtrl;
		}

		@Override
		public void onClick(View view) {
			int id = view.getId();
			DailyTask item = this.getItem(id);
			this.mDailyCtrl.selectTask(item.getName());
			this.notifyDataSetChanged();
		}

	}

	class MyTimer {

		private final Runnable mRunn;
		private Thread mCurThread;

		public MyTimer(Runnable runnable) {
			this.mRunn = runnable;
		}

		public void start() {

			Thread thd = (new Thread(new Runnable() {

				@Override
				public void run() {

					final Thread thd = Thread.currentThread();

					for (; thd.equals(MyTimer.this.mCurThread);) {
						MainActivity.this.runOnUiThread(MyTimer.this.mRunn);
						this._doSleep(1000);
					}

				}

				private void _doSleep(int ms) {
					try {
						Thread.sleep(ms);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}));

			this.mCurThread = thd;
			thd.start();
		}

		public void stop() {
			this.mCurThread = null;
		}
	}

	private void writeDefaultConfig(File conf) {
		try {
			InputStream in = this.getClass().getResourceAsStream("config.xml");
			OutputStream out = new FileOutputStream(conf);
			byte[] buff = new byte[128];
			for (int cb = in.read(buff); cb > 0; cb = in.read(buff)) {
				out.write(buff, 0, cb);
			}
			out.flush();
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_reset:
			this.onClickMenuReset();
			break;
		case R.id.menu_show_config_path:
			this.onClickMenuShowPath();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void onClickMenuShowPath() {

		String strOK = "OK";

		String path = this.mListAdapter.getDailyCtrl().getModel().getContext()
				.getConfigFile().getAbsolutePath();

		AlertDialog dlg = (new AlertDialog.Builder(this))
				.setTitle(R.string.config_path).setMessage(path)
				.setNeutralButton(strOK, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).create();
		dlg.show();
	}

	private void onClickMenuReset() {

		String strYES = "yes";
		String strNO = "no";

		AlertDialog dlg = (new AlertDialog.Builder(this))
				.setMessage(R.string.sure_to_reset)
				.setPositiveButton(strNO,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// do nothing
							}
						})
				.setNeutralButton(strYES,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								MainActivity.this.onClickMenuResetYES();
							}
						}).create();
		dlg.show();

	}

	private void onClickMenuResetYES() {
		DailyController ctrl = this.mListAdapter.getDailyCtrl();
		ctrl.reset();
		MyListAdapter adp2 = this.newAdapter();
		this.mListView.setAdapter(adp2);
		this.mListAdapter = adp2;
	}

}
