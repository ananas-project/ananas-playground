package ananas.app.point_abc.core;

import android.app.Activity;

public class DefaultThreadProvider implements IThreadProvider {

	private final Activity mAct;

	public DefaultThreadProvider(Activity act) {
		this.mAct = act;
	}

	@Override
	public void run(Runnable runnable) {
		this.mAct.runOnUiThread(runnable);
	}
}
