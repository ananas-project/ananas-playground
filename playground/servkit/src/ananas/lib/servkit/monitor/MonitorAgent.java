package ananas.lib.servkit.monitor;

public abstract class MonitorAgent {

	public static MonitorAgent getAgent() {
		return ImplMonitorAgent.getAgentInstance();
	}

	public abstract IMonitorProbe newProbe(Object target);

	public abstract IMonitor getMonitor();

	public abstract void setMonitor(IMonitor monitor);

}
