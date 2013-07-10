package ananas.lib.blueprint3.loader.eom.target;

public class Tar_element extends TargetBase {

	private String mType;
	private String mName;
	private Tar_class mParent;

	public void setType(String type) {
		this.mType = type;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public String getName() {
		return this.mName;
	}

	public String getType() {
		return this.mType;
	}

	@Override
	public void setParent(ITargetNode node) {
		this.mParent = (Tar_class) node;
	}

	@Override
	public ITargetNode getParent() {
		return this.mParent;
	}

}
