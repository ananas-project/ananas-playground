package ananas.app.boluoime;

import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.inputmethod.InputConnection;

public class BoluoInputMethodService extends InputMethodService {

	@Override
	public View onCreateInputView() {
		EmojiPane btn = new EmojiPane(this, null);
		/*
		 * btn.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { __onClickHello(); } });
		 */
		return btn;
	}

	private void __onClickHello() {
		InputConnection conn = this.getCurrentInputConnection();
		conn.commitText("Hello", 1);
	}
}
