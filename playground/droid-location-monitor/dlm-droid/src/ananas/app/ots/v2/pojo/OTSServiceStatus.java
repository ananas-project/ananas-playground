package ananas.app.ots.v2.pojo;

public class OTSServiceStatus extends POJO {

	private OTSLocation location;
	private boolean running;

	public OTSServiceStatus() {
	}

	public OTSServiceStatus(OTSServiceStatus init) {
		this.location = init.location;
		this.running = init.running;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public OTSLocation getLocation() {
		return location;
	}

	public void setLocation(OTSLocation location) {
		this.location = location;
	}

}
