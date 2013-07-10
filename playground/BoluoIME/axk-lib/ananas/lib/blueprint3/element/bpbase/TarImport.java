package ananas.lib.blueprint3.element.bpbase;

public class TarImport extends TarObject {

	private String mType;
	private String mValue;

	public void setType(String type) {
		this.mType = type;
	}

	public String getType() {
		return this.mType;
	}

	public void setValue(String value) {
		this.mValue = value;

	}

	public String getValue() {
		return this.mValue;
	}
}
