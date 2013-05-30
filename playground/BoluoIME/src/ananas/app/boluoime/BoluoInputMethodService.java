package ananas.app.boluoime;

import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputConnection;
import android.widget.Button;

public class BoluoInputMethodService extends InputMethodService {

	@Override
	public View onCreateInputView() {
		Button btn = new Button(this);
		btn.setText("hello");
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				__onClickHello();
			}
		});
		return btn;
	}

	private void __onClickHello() {
		InputConnection conn = this.getCurrentInputConnection();
		conn.commitText("Hello", 1);
	}
}
