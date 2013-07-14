package ananas.lib.blueprint3.loader.reflect.dom;

public abstract class RefElement extends RefNode {

	public RefElement(RefDocument ownerDoc) {
		super(ownerDoc);
	}

	public abstract boolean setAttribute(String attrURI, String attrLName,
			String attrValue);

}
