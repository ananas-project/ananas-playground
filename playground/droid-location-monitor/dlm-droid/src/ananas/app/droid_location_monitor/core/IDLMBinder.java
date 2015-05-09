package ananas.app.droid_location_monitor.core;

public interface IDLMBinder {

	void load();

	void save();

	void stop();

	void start();

	boolean isRunning();

	String getStatusInfo();
}
