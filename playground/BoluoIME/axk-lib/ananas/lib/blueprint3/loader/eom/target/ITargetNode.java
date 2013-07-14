package ananas.lib.blueprint3.loader.eom.target;

public interface ITargetNode {

	void setParent(ITargetNode node);

	ITargetNode getParent();

	ITargetNode[] getChildren();

	String getTargetId();

}
