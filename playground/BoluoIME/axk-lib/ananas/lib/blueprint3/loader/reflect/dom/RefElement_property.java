package ananas.lib.blueprint3.loader.reflect.dom;

import java.io.PrintStream;

public class RefElement_property extends RefElement {

	private String mKey;
	private String mValue;

	public RefElement_property(RefDocument ownerDoc) {
		super(ownerDoc);
	}

	@Override
	public boolean setAttribute(String attrURI, String attrLName,
			String attrValue) {

		if (attrLName == null) {
			return false;

		} else if (attrLName.equals("key")) {
			this.mKey = attrValue;

		} else if (attrLName.equals("value")) {
			this.mValue = attrValue;

		} else {
			return false;
		}
		return true;
	}

	@Override
	public boolean appendChild(RefNode child) {

		return false;
	}

	public String getKey() {
		return this.mKey;
	}

	public String getValue() {
		return this.mValue;
	}

	@Override
	public void printSelf(PrintStream out) {
		// TODO Auto-generated method stub

	}

}
