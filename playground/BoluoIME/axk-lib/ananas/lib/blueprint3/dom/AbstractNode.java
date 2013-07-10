package ananas.lib.blueprint3.dom;

public class AbstractNode implements BPNode {

	private BPDocument mOwnerDoc;
	private BPNode mParent;

	@Override
	public boolean appendChild(BPNode newChild) {
		return false;
	}

	@Override
	public boolean removeChild(BPNode theChild) {
		return false;
	}

	@Override
	public void setParent(BPNode newParent) {
		this.mParent = newParent;
	}

	@Override
	public BPNode getParent() {
		return this.mParent;
	}

	@Override
	public BPDocument getOwnerDocument() {
		return this.mOwnerDoc;
	}

	@Override
	public boolean bindOwnerDocument(BPDocument ownerDoc) {
		if ((this.mOwnerDoc == null) && (ownerDoc != null)) {
			this.mOwnerDoc = ownerDoc;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getLocalName() {
		return null;
	}

	@Override
	public String getNamespaceURI() {
		return null;
	}

}
