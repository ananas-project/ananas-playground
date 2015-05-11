package ananas.app.ots.v2.pojo;

public class OTSLocation extends POJO {

	private float accuracy;
	private double altitude;
	private double latitude;
	private double longitude;
	private long timestamp;

	public OTSLocation() {
	}

	public OTSLocation(OTSLocation init) {
		this.accuracy = init.accuracy;
		this.altitude = init.altitude;
		this.latitude = init.latitude;
		this.longitude = init.longitude;
		this.timestamp = init.timestamp;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public float getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

}
