package ananas.app.ots.v2.pojo;

public class OTSLocation extends POJO {

	private float accuracy;
	private float bearing;
	private float speed;
	private double altitude;
	private double latitude;
	private double longitude;
	private String provider;
	private String coordinateSystem;
	private SateTime satelliteTime;
	private long deviceTime;

	public OTSLocation() {
		this.provider = "";
		this.satelliteTime = new SateTime(0);
	}

	public OTSLocation(OTSLocation init) {
		this.accuracy = init.accuracy;
		this.bearing = init.bearing;
		this.speed = init.speed;
		this.altitude = init.altitude;
		this.latitude = init.latitude;
		this.longitude = init.longitude;
		this.provider = init.provider;
		this.satelliteTime = init.satelliteTime;
		this.deviceTime = init.deviceTime;
	}

	public String getCoordinateSystem() {
		return coordinateSystem;
	}

	public void setCoordinateSystem(String coordinateSystem) {
		this.coordinateSystem = coordinateSystem;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public float getBearing() {
		return bearing;
	}

	public void setBearing(float bearing) {
		this.bearing = bearing;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public SateTime getSatelliteTime() {
		return satelliteTime;
	}

	public void setSatelliteTime(SateTime satelliteTime) {
		this.satelliteTime = satelliteTime;
	}

	public long getDeviceTime() {
		return deviceTime;
	}

	public void setDeviceTime(long deviceTime) {
		this.deviceTime = deviceTime;
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
