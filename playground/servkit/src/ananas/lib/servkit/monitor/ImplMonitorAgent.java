package ananas.lib.servkit.monitor;

class ImplMonitorAgent extends MonitorAgent {

	private static MonitorAgent sInst = null;
	private static MonitorAgent sInst_safe = null;

	public static MonitorAgent getAgentInstance() {
		if (sInst == null) {
			sInst = _getInstSafe();
		}
		return sInst;
	}

	private synchronized static MonitorAgent _getInstSafe() {
		if (sInst_safe == null) {
			sInst_safe = new ImplMonitorAgent();
		}
		return sInst_safe;
	}

	private final MyMonitorProxy mMonitorProxy = new MyMonitorProxy();

	private ImplMonitorAgent() {
	}

	@Override
	public IMonitorProbe newProbe(Object target) {
		IMonitor monitor = this.mMonitorProxy;
		return new ImplMonitorProbe(monitor, target);
	}

	@Override
	public IMonitor getMonitor() {
		return this.mMonitorProxy;
	}

	@Override
	public void setMonitor(IMonitor monitor) {
		this.mMonitorProxy.mTarget = monitor;
	}

	private static class MyMonitorProxy implements IMonitor {

		public IMonitor mTarget;

		@Override
		public void print(IMonitorProbe probe, String message) {
			IMonitor monitor = this.mTarget;
			if (monitor == null)
				return;
			monitor.print(probe, message);
		}

		@Override
		public boolean enable() {
			IMonitor monitor = this.mTarget;
			if (monitor == null)
				return false;
			return monitor.enable();
		}
	}
}
