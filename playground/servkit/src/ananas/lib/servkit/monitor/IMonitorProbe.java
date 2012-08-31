package ananas.lib.servkit.monitor;

public interface IMonitorProbe {

	void print(String message);

	Object getTarget();

	boolean enable();

}
