package ananas.app.ots.v2.pojo;

/***
 * Satellite Time
 * */

public class SateTime {

	private long time;

	public SateTime() {
	}

	public SateTime(long t) {
		this.time = t;
	}

	public SateTime(SateTime init) {
		this.time = init.time;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
