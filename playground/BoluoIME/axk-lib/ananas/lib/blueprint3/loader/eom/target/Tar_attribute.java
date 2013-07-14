package ananas.lib.blueprint3.loader.eom.target;

public class Tar_attribute extends TargetBase {

	private String mName;
	private Tar_class mParent;

	public void setName(String name) {
		this.mName = name;
	}

	public String getName() {
		return this.mName;
	}

	@Override
	public void setParent(ITargetNode node) {
		this.mParent = (Tar_class) node;
	}

	public ITargetNode getParent() {
		return this.mParent;
	}

}
