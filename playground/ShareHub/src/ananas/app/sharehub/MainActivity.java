package ananas.app.sharehub;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		StringBuilder sb = new StringBuilder();

		Intent intent = this.getIntent();
		String data, type, action, exSubject, exText, ext;
		data = intent.getDataString();
		type = intent.getType();
		action = intent.getAction();
		exSubject = this._getExtra(intent, Intent.EXTRA_SUBJECT);
		exText = this._getExtra(intent, Intent.EXTRA_TEXT);
		ext = this._getExtraTable(intent);

		sb.append("\n      data:" + data);
		sb.append("\n      type:" + type);
		sb.append("\n    action:" + action);
		sb.append("\n       ext:" + ext);
		// sb.append("\n exSubject:" + exSubject);
		// sb.append("\n    exText:" + exText);

		TextView text = (TextView) this.findViewById(R.id.output);
		text.setText(sb.toString());

		if (type != null) {

			String msg = this._getExtra(intent, "sms_body");
			String imgurl = this._getExtra(intent, Intent.EXTRA_STREAM);

			this._saveTextToClipboard(msg);
			this._saveImageToClipDir(imgurl);

			Intent next = new Intent(this, ResultActivity.class);
			next.putExtra(Intent.EXTRA_STREAM, "" + imgurl);
			this.startActivity(next);

			if (type.startsWith("image")) {
			} else if (type.startsWith("text")) {
			}
		}

	}

	private void _saveImageToClipDir(String imgurl) {

		InputStream is = null;
		OutputStream os = null;

		try {

			String prefix = "file://";
			if (!imgurl.startsWith(prefix)) {
				return;
			}
			String srcpath = imgurl.substring(prefix.length());
			File srcf = new File(srcpath);

			File imgf = Const.getImageFile();
			imgf.getParentFile().mkdirs();
			if (imgf.exists()) {
				imgf.delete();
			}
			imgf.createNewFile();

			is = new FileInputStream(srcf);
			os = new FileOutputStream(imgf);

			byte[] buf = new byte[128];
			for (int cb = is.read(buf); cb > 0; cb = is.read(buf)) {
				os.write(buf, 0, cb);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			is.close();
		} catch (Exception e) {
			// e.printStackTrace();
		}
		try {
			os.close();
		} catch (Exception e) {
			// e.printStackTrace();
		}

	}

	private void _saveTextToClipboard(String msg) {
		ClipboardManager clipb = (ClipboardManager) this
				.getSystemService(Service.CLIPBOARD_SERVICE);

		clipb.setText(msg);
	}

	private String _getExtraTable(Intent intent) {

		try {

			StringBuilder sb = new StringBuilder();

			Bundle ext = intent.getExtras();
			Set<String> keys = ext.keySet();
			for (String key : keys) {
				String value = ext.get(key).toString();
				sb.append("\n" + key + "=" + value + ";");
			}

			return sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private String _getExtra(Intent intent, String key) {
		try {

			return intent.getExtras().get(key).toString();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
