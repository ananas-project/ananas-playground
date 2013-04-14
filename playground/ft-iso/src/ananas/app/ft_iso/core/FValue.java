package ananas.app.ft_iso.core;

public class FValue {

	private final double mValue;

	public FValue(double value) {
		final double min = 0.000001;
		this.mValue = (value < min) ? min : value;
	}

	public String toString() {
		String ret = "F" + this.mValue;
		return ret;
	}

	public double toDouble() {
		return this.mValue;
	}

	public static FValue getF(IsoValue iso, Light light, TimeValue time) {
		double i = iso.toDouble();
		double l = light.toDouble();
		double t = time.toDouble();
		double f = Math.sqrt((i * t) / l);
		return new FValue(f);
	}
}
