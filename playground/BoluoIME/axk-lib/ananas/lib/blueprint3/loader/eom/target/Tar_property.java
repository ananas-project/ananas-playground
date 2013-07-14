package ananas.lib.blueprint3.loader.eom.target;

public class Tar_property extends TargetBase {

	private String mKey;
	private String mValue;

	public void setKey(String key) {
		this.mKey = key;
	}

	public void setValue(String value) {
		this.mValue = value;
	}

	public String getKey() {
		return this.mKey;
	}

	public String getValue() {
		return this.mValue;
	}

	@Override
	public void setParent(ITargetNode node) {
	}

	@Override
	public ITargetNode getParent() {
		return null;
	}

}
