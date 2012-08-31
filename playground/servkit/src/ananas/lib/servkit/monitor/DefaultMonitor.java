package ananas.lib.servkit.monitor;

public class DefaultMonitor implements IMonitor {

	@Override
	public void print(IMonitorProbe probe, String message) {
		System.out.println("monitor : " + probe.getTarget() + " : " + message);
	}

	@Override
	public boolean enable() {
		return true;
	}

}
