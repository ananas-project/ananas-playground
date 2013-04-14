package ananas.app.ft_iso;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

	private ValueBox m_a_iso;
	private ValueBox m_a_time;
	private ValueBox m_a_f;
	private ValueBox m_b_f;
	private ValueBox m_b_iso;
	private ValueBox m_b_time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.m_a_f = (ValueBox) this.findViewById(R.id.vbox_a_f);
		this.m_a_iso = (ValueBox) this.findViewById(R.id.vbox_a_iso);
		this.m_a_time = (ValueBox) this.findViewById(R.id.vbox_a_time);

		this.m_b_f = (ValueBox) this.findViewById(R.id.vbox_b_f);
		this.m_b_iso = (ValueBox) this.findViewById(R.id.vbox_b_iso);
		this.m_b_time = (ValueBox) this.findViewById(R.id.vbox_b_time);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
