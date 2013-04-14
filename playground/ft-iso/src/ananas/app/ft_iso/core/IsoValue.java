package ananas.app.ft_iso.core;

public class IsoValue {

	private final int mValue;

	public IsoValue(int value) {
		final int min = 1;
		this.mValue = (value < min) ? min : value;
	}

	public String toString() {
		String ret = "ISO" + this.mValue;
		return ret;
	}

	public double toDouble() {
		return this.mValue;
	}

}
