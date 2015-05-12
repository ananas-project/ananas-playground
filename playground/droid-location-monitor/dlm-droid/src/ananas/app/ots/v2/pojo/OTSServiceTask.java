package ananas.app.ots.v2.pojo;

public class OTSServiceTask extends POJO {

	private String taskId;
	private long startTime;
	private boolean working;

	public OTSServiceTask() {
		this.taskId = "undef";
	}

	public OTSServiceTask(OTSServiceTask init) {
		this.taskId = init.taskId;
		this.startTime = init.startTime;
		this.working = init.working;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public boolean isWorking() {
		return working;
	}

	public void setWorking(boolean working) {
		this.working = working;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

}
