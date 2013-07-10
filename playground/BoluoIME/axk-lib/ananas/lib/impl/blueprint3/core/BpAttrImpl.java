package ananas.lib.impl.blueprint3.core;

import ananas.lib.blueprint3.dom.BPAttribute;
import ananas.lib.blueprint3.dom.BPDocument;
import ananas.lib.blueprint3.dom.BPNode;

public class BpAttrImpl implements BPAttribute {

	private String mURI;
	private String mLocalName;
	private String mValue;

	public BpAttrImpl(String uri, String localName, String value) {
		this.mURI = uri;
		this.mLocalName = localName;
		this.mValue = value;
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
	public String getLocalName() {
		return this.mLocalName;
	}

	@Override
	public String getNamespaceURI() {
		return this.mURI;
	}

	@Override
	public String getValue() {
		return this.mValue;
	}

}
