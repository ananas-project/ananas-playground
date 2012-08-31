package ananas.lib.servkit.monitor;

public interface IMonitor {

	void print(IMonitorProbe probe, String message);

	boolean enable();

}
