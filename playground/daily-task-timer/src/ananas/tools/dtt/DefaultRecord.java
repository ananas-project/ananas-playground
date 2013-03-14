package ananas.tools.dtt;

public class DefaultRecord implements DailyRecord {

	private final String mName;
	private final long mMS;

	public DefaultRecord(long ms, String name) {
		this.mMS = ms;
		this.mName = name;
	}

	@Override
	public long getTimestamp() {
		return this.mMS;
	}

	@Override
	public String getName() {
		return this.mName;
	}

}
