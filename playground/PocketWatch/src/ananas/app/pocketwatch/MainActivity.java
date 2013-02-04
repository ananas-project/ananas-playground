package ananas.app.pocketwatch;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView mTextHMS;
	private TextView mTextYMD;
	private TextView mTextMS;
	private MyTimer mTimer;
	private Calendar mCalendar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.mTextYMD = (TextView) this.findViewById(R.id.text_ymd);
		this.mTextHMS = (TextView) this.findViewById(R.id.text_hms);
		this.mTextMS = (TextView) this.findViewById(R.id.text_ms);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		System.out.println("stop timer");
		this.mTimer = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("start timer");
		MyTimer timer = new MyTimer();
		this.mTimer = timer;
		timer.start();
	}

	private static int s_IdGen = 0;

	class MyTimer implements Runnable {

		private final int mId;

		public MyTimer() {
			this.mId = (++s_IdGen);
		}

		public void start() {
			(new Thread(this)).start();
		}

		@Override
		public void run() {
			System.out.println("The timer[" + this.mId + "] is started.");
			for (; this.equals(MainActivity.this.mTimer);) {
				this.fire();
				this.sleep(93);
			}
			System.out.println("The timer[" + this.mId + "] is stopped.");
		}

		private void sleep(int ms) {
			try {
				Thread.sleep(ms);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		private final Runnable mFireRunnable = new Runnable() {

			@Override
			public void run() {
				MainActivity.this.onTimer();
			}
		};

		private void fire() {
			MainActivity.this.runOnUiThread(this.mFireRunnable);
		}
	}

	protected void onTimer() {
		final long now = System.currentTimeMillis();

		Calendar cal = this._getCalendar();
		cal.setTimeInMillis(now);

		int yy, mm, dd;
		yy = cal.get(Calendar.YEAR);
		mm = cal.get(Calendar.MONTH) + 1;
		dd = cal.get(Calendar.DAY_OF_MONTH);
		int h, m, s;
		h = cal.get(Calendar.HOUR_OF_DAY);
		m = cal.get(Calendar.MINUTE);
		s = cal.get(Calendar.SECOND);

		this.mTextYMD.setText(yy + "-" + mm + "-" + dd);
		this.mTextHMS.setText(h + ":" + m + ":" + s);

		String str = ".000" + now;
		this.mTextMS.setText("." + str.substring(str.length() - 3));
	}

	private Calendar _getCalendar() {
		Calendar cal = this.mCalendar;
		if (cal == null) {
			TimeZone zone = TimeZone.getTimeZone("GMT+0");
			cal = Calendar.getInstance(zone);
			this.mCalendar = cal;
		}
		return cal;
	}

}
