package ananas.app.point_abc.core;

import android.app.Activity;
import android.app.AlertDialog;

public class DefaultPointABCClientListener implements IPointABCClientListener {

	private final Activity mActivity;

	private final IPointABCClientListener mInnerListener = new MyInnerListener();

	public DefaultPointABCClientListener(Activity activity) {
		this.mActivity = activity;
	}

	@Override
	public void onEvent(IPointABCClient client, int code, String message) {
		MyRunnable runn = new MyRunnable(client, code, message,
				this.mInnerListener);
		this.mActivity.runOnUiThread(runn);
	}

	class MyRunnable implements Runnable {

		private IPointABCClientListener mTarget;
		private int mCode;
		private IPointABCClient mClient;
		private String mMsg;

		public MyRunnable(IPointABCClient client, int code, String message,
				IPointABCClientListener l) {
			this.mClient = client;
			this.mCode = code;
			this.mMsg = message;
			this.mTarget = l;
		}

		@Override
		public void run() {
			this.mTarget.onEvent(mClient, mCode, mMsg);
		}

	}

	class MyInnerListener implements IPointABCClientListener {

		@Override
		public void onEvent(IPointABCClient client, int code, String message) {
			__onEvent(client, code, message);
		}
	}

	class MyDialogProvider {

		private AlertDialog mCache;

		public AlertDialog getDialog(IPointABCClient client, int code,
				String message) {
			String title = "code:" + code;
			AlertDialog dlg = this.mCache;
			if (dlg == null) {
				dlg = (new AlertDialog.Builder(mActivity)).setTitle(title)
						.setMessage(message).create();
				this.mCache = dlg;
			}
			dlg.setTitle(title);
			dlg.setMessage(message);
			return dlg;
		}
	}

	final MyDialogProvider mOut = new MyDialogProvider();
	final MyDialogProvider mErr = new MyDialogProvider();

	private void __onEvent(IPointABCClient client, int code, String message) {
		AlertDialog dlg;
		switch (code) {
		case IPointABCClientListener.code_begin:
			dlg = this.mOut.getDialog(client, code, message);
			dlg.show();
			break;
		case IPointABCClientListener.code_end:
			dlg = this.mOut.getDialog(client, code, message);
			dlg.hide();
			break;
		case IPointABCClientListener.code_error:
			dlg = this.mErr.getDialog(client, code, message);
			dlg.show();
			break;
		}

	}

}
