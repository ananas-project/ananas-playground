package ananas.lib.blueprint3.loader.eom.target;

public abstract class TargetBase implements ITargetNode {

	@Override
	public ITargetNode[] getChildren() {
		return null;
	}

	@Override
	public String getTargetId() {
		return (getClass().getName() + '@' + Integer.toHexString(hashCode()));
	}

}
