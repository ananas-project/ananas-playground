package ananas.app.droid_location_monitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ananas.app.droid_location_monitor.core.WorkingDirectory;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TripLogActivity extends Activity {

	private TextView mTextTime;
	private EditText mEditTitle;
	private EditText mEditContent;
	private Button mButtonDoIt;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trip_log);

		this.mTextTime = (TextView) this.findViewById(R.id.text_time);
		this.mEditContent = (EditText) this.findViewById(R.id.edit_content);
		this.mEditTitle = (EditText) this.findViewById(R.id.edit_title);
		this.mButtonDoIt = (Button) this.findViewById(R.id.button_do_it);

		this.mButtonDoIt.setText("the do it");
		this.mEditContent.setText("the content");
		this.mEditTitle.setText("the title");
		this.mTextTime.setText("the time");

		this.mButtonDoIt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				TripLogActivity.this.onClickDoIt();
			}
		});

		this.dataToUI();

	}

	protected void onClickDoIt() {
		this.uiToData();
		MyLogRec rec = this.mCurRec;
		if (rec == null) {
			this._newLog();
		} else {
			this._saveLog(rec);
		}
		this.dataToUI();
	}

	private void _newLog() {
		long now = System.currentTimeMillis();
		this.mCurRec = new MyLogRec(now);
	}

	private void _saveLog(MyLogRec rec) {

		String title = rec.mTextTitle;
		if (title == null) {
			title = "";
		} else {
			title = title.trim();
		}
		if (title.length() <= 0) {
			(new AlertDialog.Builder(this)).setTitle("Tip")
					.setMessage("The TITLE is required!")
					.setPositiveButton("OK", new AlertDialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// do nothing
						}
					}).show();
			return;
		}

		File file = this.getOutputFile(rec);
		try {
			rec.mTimeSave = System.currentTimeMillis();
			RecFile rf = new RecFile(file);

			rf.setHeaderField("Content-Type", "application/text-trip-log");
			rf.setHeaderField("the-create-time",
					this.timeToString(rec.mTimeCreate, "-", " ", ":"));
			rf.setHeaderField("the-save-time",
					this.timeToString(rec.mTimeSave, "-", " ", ":"));

			rf.setTimeCreate(rec.mTimeCreate);
			rf.setTimeSave(rec.mTimeSave);
			rf.setTitle(rec.mTextTitle);
			rf.setText(rec.mTextContent);
			rf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		(new AlertDialog.Builder(this)).setTitle("Tip")
				.setMessage("The LOG has been saved to " + file)
				.setPositiveButton("OK", new AlertDialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// do nothing
					}
				}).show();

		this.mCurRec = null;
	}

	private File getOutputFile(MyLogRec rec) {

		// dir path
		File dir = WorkingDirectory.getRecordDirectory();

		// file name

		String filename = "triplog_"
				+ this.timeToString(rec.mTimeCreate, "", "_", "") + ".txt";
		// return
		return new File(dir, filename);
	}

	private MyLogRec mCurRec;

	private static class MyLogRec {

		private final long mTimeCreate;
		private long mTimeSave;
		private String mTextTitle;
		private String mTextContent;

		public MyLogRec(long timeCreate) {
			this.mTimeCreate = timeCreate;
		}
	}

	private void uiToData() {
		MyLogRec rec = this.mCurRec;
		if (rec != null) {
			rec.mTextTitle = this.mEditTitle.getText().toString();
			rec.mTextContent = this.mEditContent.getText().toString();
		}
	}

	private void dataToUI() {
		MyLogRec rec = this.mCurRec;
		if (rec != null) {
			this.mEditTitle.setText(rec.mTextTitle);
			this.mEditContent.setText(rec.mTextContent);
			this.mButtonDoIt.setText("Save");
			this.mTextTime.setText(this.timeToString(rec.mTimeCreate, "-", " ",
					":"));
			boolean enable = true;
			this.mEditTitle.setEnabled(enable);
			this.mEditContent.setEnabled(enable);
		} else {
			this.mEditTitle.setText("");
			this.mEditContent.setText("");
			this.mTextTime.setText("Trip Log");
			this.mButtonDoIt.setText("+");
			boolean enable = false;
			this.mEditTitle.setEnabled(enable);
			this.mEditContent.setEnabled(enable);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private String timeToString(long time, String splitYMD, String splitGap,
			String splitHMS) {

		Calendar cale = Calendar.getInstance();
		cale.setTimeInMillis(time);

		int yy = cale.get(Calendar.YEAR);
		int mm = cale.get(Calendar.MONTH);
		int dd = cale.get(Calendar.DAY_OF_MONTH);

		int h = cale.get(Calendar.HOUR_OF_DAY);
		int m = cale.get(Calendar.MINUTE);
		int s = cale.get(Calendar.SECOND);

		return (intToStr(yy, 4) + splitYMD + intToStr(mm, 2) + splitYMD
				+ intToStr(dd, 2) + splitGap + intToStr(h, 2) + splitHMS
				+ intToStr(m, 2) + splitHMS + intToStr(s, 2));
	}

	private String intToStr(int n, int width) {
		String s = n + "";
		for (; s.length() < width;) {
			s = "0" + s;
		}
		return s;
	}

	class RecFile {

		private final File mFile;
		private String mText;
		private final Map<String, String> mHeaders = new HashMap<String, String>();

		public RecFile(File file) {
			this.mFile = file;
		}

		public void setText(String s) {
			this.mText = s + "";
		}

		public void setTitle(String s) {
			this.mHeaders.put("Title", s + "");
		}

		public void setTimeCreate(long time) {
			this.mHeaders.put("Create-Time", time + "");
		}

		public void setTimeSave(long time) {
			this.mHeaders.put("Save-Time", time + "");
		}

		public void setHeaderField(String key, String value) {
			this.mHeaders.put(key, value);
		}

		public void close() throws IOException {
			final String CRLF = "\r\n";
			boolean isNew = !this.mFile.exists();
			if (isNew) {
				this.mFile.getParentFile().mkdirs();
				this.mFile.createNewFile();
			}
			final OutputStream out = new FileOutputStream(this.mFile);
			// flush head
			final StringBuilder sb = new StringBuilder();
			sb.append("HTDF/1.0" + CRLF);
			for (String key : this.mHeaders.keySet()) {
				String value = this.mHeaders.get(key);
				sb.append(key + ":" + value + CRLF);
			}
			sb.append(CRLF);
			out.write(sb.toString().getBytes("UTF-8"));

			// flush data
			byte[] data = this.mText.getBytes("UTF-8");
			out.write(data);
			out.flush();
			out.close();
		}
	}

}
