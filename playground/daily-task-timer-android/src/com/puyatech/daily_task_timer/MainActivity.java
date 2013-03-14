package com.puyatech.daily_task_timer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;

import ananas.lib.util.logging.AbstractLoggerFactory;
import ananas.lib.util.logging.Logger;
import ananas.tools.dtt.Const;
import ananas.tools.dtt.DailyContext;
import ananas.tools.dtt.DailyController;
import ananas.tools.dtt.DailyTask;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

public class MainActivity extends Activity {

	final static Logger logger = (new AbstractLoggerFactory() {
	}).getLogger();
	private DailyController mDttCtrl;
	// private ListView mListView;
	private MyListAdapter mListAdapter;
	private MyTimer mTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ListView lv = (ListView) this.findViewById(R.id.listView1);
		// this.mListView = lv;

		DailyController ctrl = this.initCtrl();
		this.mDttCtrl = ctrl;

		List<DailyTask> list = ctrl.getModel().getTasks();

		MyListAdapter adp = new MyListAdapter(this, list);
		this.mListAdapter = adp;
		lv.setAdapter(adp);

		lv.refreshDrawableState();

		MyTimer timer = new MyTimer(new Runnable() {

			@Override
			public void run() {
				MainActivity.this.mListAdapter.notifyDataSetChanged();
			}
		});

		this.mTimer = timer;
		// timer.start();

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

	private OnClickListener mOnItemClockListener = new OnClickListener() {

		@Override
		public void onClick(View view) {

			CheckedTextView ctv = (CheckedTextView) view;

			int pos = ctv.getId();

			DailyTask item = MainActivity.this.mListAdapter.getItem(pos);
			MainActivity.this.mDttCtrl.selectTask(item.getName());

		}
	};

	class MyListAdapter extends ArrayAdapter<DailyTask> {

		static final int id_layout = android.R.layout.simple_list_item_checked;

		public MyListAdapter(Context context, List<DailyTask> objects) {
			super(context, id_layout, objects);
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

			DailyTask hotTask = MainActivity.this.mDttCtrl.getModel()
					.getCurrentTask();
			view.setChecked(item.equals(hotTask));

			view.setOnClickListener(MainActivity.this.mOnItemClockListener);
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

	private DailyController initCtrl() {

		File dir = android.os.Environment.getExternalStorageDirectory();
		dir = new File(dir, "ananas/daily_task_timer");

		Properties prop = new Properties();
		prop.setProperty(Const.key_file_conf, dir.getAbsolutePath()
				+ "/config.xml");
		prop.setProperty(Const.key_file_current, dir.getAbsolutePath()
				+ "/record.txt");

		try {
			DailyContext context = DailyContext.Factory.getDefault(prop);

			File conf = context.getConfigFile();
			logger.trace("load config from " + conf);
			if (!conf.exists()) {
				conf.getParentFile().mkdirs();
				conf.createNewFile();
				this.writeDefaultConfig(conf);
			}

			DailyController ctrl = context.getFactory().newController();
			ctrl.load();
			return ctrl;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
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

}
