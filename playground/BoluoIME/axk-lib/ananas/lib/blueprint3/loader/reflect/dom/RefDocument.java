package ananas.lib.blueprint3.loader.reflect.dom;

import java.io.PrintStream;

public class RefDocument extends RefNode {

	public RefDocument() {
		super(null);
	}

	private RefElement_reflect mRoot;

	public RefElement_reflect getRoot() {
		return this.mRoot;
	}

	public RefElement createElement(String uri, String localName) {
		if (localName == null) {

		} else if (localName.equals("element")) {
			return new RefElement_element(this);

		} else if (localName.equals("namespace")) {
			return new RefElement_namespace(this);

		} else if (localName.equals("property")) {
			return new RefElement_property(this);

		} else if (localName.equals("reflect")) {
			return new RefElement_reflect(this);

		} else {
		}
		return null;
	}

	@Override
	public boolean appendChild(RefNode child) {
		if (child instanceof RefElement_reflect) {
			this.mRoot = (RefElement_reflect) child;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void printSelf(PrintStream out) {
		out.println("begin " + this + ".domTree:");
		RefElement_reflect root = this.mRoot;
		if (root != null) {
			root.printSelf(out);
		}
		out.println("end " + this + ".domTree:");
	}

}
