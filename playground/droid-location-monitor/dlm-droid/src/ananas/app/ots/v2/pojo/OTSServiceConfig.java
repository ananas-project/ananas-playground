package ananas.app.ots.v2.pojo;

public class OTSServiceConfig extends POJO {

	private int lastLocationQueueSizeMax;

	public OTSServiceConfig() {
	}

	public OTSServiceConfig(OTSServiceConfig conf) {
	}

	public int getLastLocationQueueSizeMax() {
		return lastLocationQueueSizeMax;
	}

	public void setLastLocationQueueSizeMax(int lastLocationQueueSizeMax) {
		this.lastLocationQueueSizeMax = lastLocationQueueSizeMax;
	}

}
