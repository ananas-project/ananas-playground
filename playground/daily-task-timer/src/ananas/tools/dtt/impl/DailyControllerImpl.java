package ananas.tools.dtt.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import ananas.tools.dtt.DailyController;
import ananas.tools.dtt.DailyModel;
import ananas.tools.dtt.DailyRecord;
import ananas.tools.dtt.DailyTask;
import ananas.tools.dtt.DefaultRecord;

public class DailyControllerImpl implements DailyController {

	private final DailyModel mModel;

	public DailyControllerImpl(DailyModel model) {
		this.mModel = model;
	}

	@Override
	public DailyModel getModel() {
		return this.mModel;
	}

	@Override
	public void addRecord(DailyRecord rec) {
		this.mModel.addRecord(rec);
		try {
			// flush file

			String str = "$REC," + rec.getTimestamp() + "," + rec.getName()
					+ ",ok\n";

			File file = this.mModel.getContext().getRecordFile();
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file, true);
			out.write(str.getBytes("UTF-8"));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void reset() {
		this.mModel.reset();
		// flush file
		this.mModel.getContext().getRecordFile().delete();
	}

	@Override
	public void load() {
		// this.mModel.reset();
		// read file
		MyLoader ldr = new MyLoader(this.mModel);
		ldr.load();
	}

	class MyLoader {

		private DailyModel mModel;

		public MyLoader(DailyModel model) {
			this.mModel = model;
		}

		public void load() {
			try {
				this.mModel.reset();
				File file = this.mModel.getContext().getRecordFile();
				if (!file.exists()) {
					file.getParentFile().mkdirs();
					file.createNewFile();
				}
				InputStream in = new FileInputStream(file);
				InputStreamReader rdr = new InputStreamReader(in);
				StringBuilder sb = new StringBuilder();
				for (int ch = rdr.read(); ch >= 0; ch = rdr.read()) {
					if (ch == 0x0a || ch == 0x0d) {
						this.onLine(sb.toString());
						sb.setLength(0);
					} else {
						sb.append((char) ch);
					}
				}
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		private void onLine(String line) {
			final int i0 = line.indexOf(',');
			if (i0 < 0) {
				return;
			}
			final int i1 = line.indexOf(',', i0 + 1);
			if (i1 < 0) {
				return;
			}
			final int i2 = line.indexOf(',', i1 + 1);
			if (i2 < 0) {
				return;
			}
			String s0 = line.substring(0, i0).trim();
			String s1 = line.substring(i0 + 1, i1).trim();
			String s2 = line.substring(i1 + 1, i2).trim();
			if (!s0.equals("$REC")) {
				return;
			}
			DailyRecord rec = new DefaultRecord(Long.parseLong(s1), s2);
			this.mModel.addRecord(rec);
		}
	}

	@Override
	public DailyTask selectTask(String taskName) {
		DailyTask task = this.mModel.getTask(taskName);
		if (task != null) {
			long ms = System.currentTimeMillis();
			DailyRecord rec = new DefaultRecord(ms, task.getName());
			this.addRecord(rec);
		}
		return task;
	}
}
