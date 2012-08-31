package ananas.lib.servkit.monitor;

final class ImplMonitorProbe implements IMonitorProbe {

	private final Object mTarget;
	private final IMonitor mMonitor;

	public ImplMonitorProbe(IMonitor monitor, Object target) {
		this.mMonitor = monitor;
		this.mTarget = target;
		this.mMonitor.print(this, "new");
	}

	@Override
	public void print(String message) {
		this.mMonitor.print(this, message);
	}

	@Override
	public Object getTarget() {
		return this.mTarget;
	}

	@Override
	public boolean enable() {
		return this.mMonitor.enable();
	}

}
