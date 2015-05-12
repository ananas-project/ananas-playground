package ananas.app.ots.v2.pojo;

public class OTSServiceStatus extends POJO {

	private OTSLocation location;
	private String taskId;
	private long taskStartTime;
	private long countLocation;
	private boolean running;

	public OTSServiceStatus() {
	}

	public OTSServiceStatus(OTSServiceStatus init) {
		this.taskId = init.taskId;
		this.taskStartTime = init.taskStartTime;
		this.location = init.location;
		this.countLocation = init.countLocation;
		this.running = init.running;
	}

	public OTSLocation getLocation() {
		return location;
	}

	public void setLocation(OTSLocation location) {
		this.location = location;
	}

	public long getCountLocation() {
		return countLocation;
	}

	public void setCountLocation(long countLocation) {
		this.countLocation = countLocation;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public long getTaskStartTime() {
		return taskStartTime;
	}

	public void setTaskStartTime(long taskStartTime) {
		this.taskStartTime = taskStartTime;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

}
