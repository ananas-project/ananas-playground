package ananas.app.sharehub;

import java.io.File;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		ImageView imageView = (ImageView) this.findViewById(R.id.imageView);
		TextView textInfo = (TextView) this.findViewById(R.id.text_info);
		TextView textContent = (TextView) this.findViewById(R.id.text_content);

		String imgurl = this.getIntent().getStringExtra(Intent.EXTRA_STREAM);
		String txtCont = this._getClipboardText();

		textContent.setText(txtCont);
		textInfo.setText("text has been copied into the clipboard.\n" + imgurl);

		File file = Const.getImageFile();
		// String url = "file://" + file.getAbsolutePath() ;
		Uri uri = Uri.fromFile(file);
		imageView.setImageURI(uri);

	}

	private String _getClipboardText() {
		ClipboardManager cm = (ClipboardManager) this
				.getSystemService(Service.CLIPBOARD_SERVICE);
		return cm.getText().toString();
	}

}
