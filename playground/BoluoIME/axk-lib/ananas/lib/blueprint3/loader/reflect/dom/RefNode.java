package ananas.lib.blueprint3.loader.reflect.dom;

import java.io.PrintStream;

public abstract class RefNode {

	private final RefDocument mOwnerDoc;

	public RefNode(RefDocument ownerDoc) {
		this.mOwnerDoc = ownerDoc;
	}

	public abstract boolean appendChild(RefNode child);

	public abstract void printSelf(PrintStream out);

	public RefDocument getOwnerDocument() {
		return this.mOwnerDoc;
	}

}
