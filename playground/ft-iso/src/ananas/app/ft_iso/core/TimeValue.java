package ananas.app.ft_iso.core;

public class TimeValue {

	private final double mSec;

	public TimeValue(double sec) {
		double min = 0.0000001;
		this.mSec = (sec < min) ? min : sec;
	}

	public String toString() {
		double sec = this.mSec;
		if (sec >= 0.999999) {
			return this.mSec + "s";
		} else {
			double v = 1 / sec;
			return "1/" + v + "s";
		}
	}

	public double toDouble() {
		return this.mSec;
	}

	public static TimeValue getTime(FValue f, IsoValue iso, Light light) {
		double f2 = f.toDouble();
		double i = iso.toDouble();
		double l = light.toDouble();
		double t = (f2 * f2 * l) / i;
		return new TimeValue(t);
	}

}
