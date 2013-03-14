package com.puyatech.daily_task_timer;

import java.io.File;
import java.util.Properties;

import ananas.tools.dtt.Const;
import ananas.tools.dtt.DailyContext;
import ananas.tools.dtt.DailyController;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		DailyController ctrl = this.initCtrl();
		ctrl.getModel();

	}

	private DailyController initCtrl() {

		File dir = android.os.Environment.getExternalStorageDirectory();
		dir = new File(dir, "ananas/daily_task_timer");

		Properties prop = new Properties();
		prop.setProperty(Const.key_file_conf, dir.getAbsolutePath()
				+ "config.xml");
		prop.setProperty(Const.key_file_current, dir.getAbsolutePath()
				+ "record.txt");

		try {
			DailyContext context = DailyContext.Factory.getDefault(prop);

			File conf = context.getConfigFile();
			if (!conf.exists()) {
				conf.getParentFile().mkdirs();
				conf.createNewFile();
			}

			DailyController ctrl = context.getFactory().newController();
			ctrl.load();
			return ctrl;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
