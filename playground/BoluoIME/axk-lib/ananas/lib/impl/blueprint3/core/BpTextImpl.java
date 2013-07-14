package ananas.lib.impl.blueprint3.core;

import ananas.lib.blueprint3.dom.BPDocument;
import ananas.lib.blueprint3.dom.BPNode;
import ananas.lib.blueprint3.dom.BPText;

public class BpTextImpl implements BPText {

	private final String mData;

	public BpTextImpl(String data) {
		this.mData = data;
	}

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
	}

	@Override
	public BPNode getParent() {
		return null;
	}

	@Override
	public BPDocument getOwnerDocument() {
		return null;
	}

	@Override
	public boolean bindOwnerDocument(BPDocument ownerDoc) {
		return false;
	}

	@Override
	public String getData() {
		return this.mData;
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
