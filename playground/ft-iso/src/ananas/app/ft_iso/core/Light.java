package ananas.app.ft_iso.core;

public class Light {

	private final double mValue;

	public Light(double value) {
		final double min = 0.00000000001;
		this.mValue = (value < min) ? min : value;
	}

	public double toDouble() {
		return this.mValue;
	}

	public String toString() {
		return "L" + this.mValue;
	}

	public static Light getLight(FValue f, IsoValue iso, TimeValue time) {
		double f2 = f.toDouble();
		double i = iso.toDouble();
		double t = time.toDouble();
		double v = (i * t) / (f2 * f2);
		return new Light(v);
	}

}
