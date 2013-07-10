package ananas.lib.axk.engine.util;

import ananas.lib.axk.engine.XContext;
import ananas.lib.axk.engine.XEngine;
import ananas.lib.axk.engine.XEngineListener;
import ananas.lib.axk.engine.XPhase;

public class DefaultEngineRunner implements EngineRunner {

	private final XContext mContext;

	public DefaultEngineRunner(XContext context) {
		this.mContext = context;
	}

	@Override
	public void run() {
		try {
			XEngine engine = this.mContext.getEngineFactory().createEngine();
			XEngineListener l = this.mContext.getEngineListener();
			l.onPhaseChanged(mContext, XPhase.connect);
			engine.run(mContext);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
